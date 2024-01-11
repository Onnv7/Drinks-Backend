package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.*;
import com.hcmute.drink.collection.embedded.*;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateOrderRequest;
import com.hcmute.drink.dto.request.CreateReviewRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.service.database.IOrderService;
import com.hcmute.drink.service.elasticsearch.OrderSearchService;
import com.hcmute.drink.utils.ModelMapperUtils;
import com.hcmute.drink.utils.VNPayUtils;
import com.hcmute.drink.repository.database.OrderRepository;
import com.hcmute.drink.utils.MongoDbUtils;
import com.hcmute.drink.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.hcmute.drink.constant.VNPayConstant.*;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final SecurityUtils securityUtils;
    private final VNPayUtils vnPayUtils;
    private final MongoDbUtils mongoDbUtils;
    private final ModelMapperUtils modelMapperUtils;
    private final SequenceService sequenceService;
    private final OrderSearchService orderSearchService;

    @Autowired
    @Lazy
    private TransactionService transactionService;

    public OrderCollection getById(String transactionId)  {
        return orderRepository.findByTransactionId(new ObjectId(transactionId))
                .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + transactionId));
    }

    public OrderCollection getByOrderId(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }
    public long calculateOrderBill(List<OrderDetailsEmbedded> products) {
        long totalPrice = 0;
        for (OrderDetailsEmbedded product : products) {
            ProductCollection productInfo = productService.getById(product.getProductId().toString()); //productRepository.findById(product.getProductId().toString()).orElse(null);

            double totalPriceToppings = 0;
            List<ToppingEmbedded> toppings = product.getToppings() != null ? product.getToppings() : new ArrayList<>();
            for (ToppingEmbedded topping : toppings) {
                totalPriceToppings += topping.getPrice();
            }

            SizeEmbedded size = productInfo.getSizeList().stream().filter(it -> it.getSize().equals(product.getSize())).findFirst().orElseThrow();
            totalPrice += (long) ((size.getPrice() + totalPriceToppings) * product.getQuantity());
        }
        return totalPrice;
    }
    // SERVICES =================================================================

    @Override
    @Transactional
    public CreateShippingOrderResponse createShippingOrder(CreateOrderRequest body, HttpServletRequest request) {
        PaymentType paymentType = body.getPaymentType();
        OrderCollection data = modelMapperUtils.mapClass(body, OrderCollection.class);
        userService.getById(data.getUserId().toString());

        List<OrderDetailsEmbedded> products = data.getProducts();
        long totalPrice = calculateOrderBill(products);
        data.setTotal(totalPrice);

        TransactionCollection transData = new TransactionCollection();
        CreateShippingOrderResponse resData = new CreateShippingOrderResponse();
        if (paymentType == PaymentType.CASHING) {
            transData = TransactionCollection.builder()
                    .status(PaymentStatus.UNPAID)
                    .paymentType(PaymentType.CASHING).build();
        } else {
            Map<String, String> paymentData = null;
            try {
                paymentData = vnPayUtils.createUrlPayment(request, totalPrice, "Shipping Order Info");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            transData = TransactionCollection.builder()
                    .invoiceCode(paymentData.get(VNP_TXN_REF_KEY).toString())
                    .timeCode(paymentData.get(VNP_CREATE_DATE_KEY))
                    .status(PaymentStatus.UNPAID)
                    .paymentType(paymentType)
                    .build();
            resData.setPaymentUrl(paymentData.get(VNP_URL_KEY));
        }

        TransactionCollection transSaved = transactionService.saveTransaction(transData);

        data.setOrderType(OrderType.SHIPPING);
        data.setTransactionId(new ObjectId(transSaved.getId()));
        String makerId = securityUtils.getCurrentUserId();
        OrderLogEmbedded log = OrderLogEmbedded.builder()
                .orderStatus(OrderStatus.CREATED)
                .isEmployee(false)
                .makerId(new ObjectId(makerId))
                .time(new Date()).build();
        data.setEventLogs(new ArrayList<>(Arrays.<OrderLogEmbedded>asList(log)));
        data.setCode(sequenceService.generateCode(OrderCollection.SEQUENCE_NAME, OrderCollection.PREFIX_CODE, OrderCollection.LENGTH_NUMBER));

        OrderCollection order = orderRepository.save(data);
        resData.setOrderId(order.getId());
        resData.setTransactionId(transData.getId());
        orderSearchService.createOrder(order);
        return resData;
    }

    @Override
    public void addNewOrderEvent(Maker maker, String id, OrderStatus orderStatus, String description) {
        OrderCollection order = getByOrderId(id);
        String employeeId = securityUtils.getCurrentUserId();
        order.getEventLogs().add(OrderLogEmbedded.builder()
                .orderStatus(orderStatus)
                .description(description)
                .isEmployee(maker.equals(Maker.employee))
                .makerId(new ObjectId(employeeId))
                .time(mongoDbUtils.createCurrentTime())
                .build()
        );
        orderRepository.save(order);
    }

    @Override
    public List<GetAllShippingOrdersResponse> getAllShippingOrdersQueueForEmployee() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999);
        List<GetAllShippingOrdersResponse> ordersCreatedOnDate = orderRepository.getAllShippingOrdersQueueForEmployee(Date.from(startOfDay.toInstant()), Date.from(endOfDay.toInstant()));

        return ordersCreatedOnDate;
    }

    @Override
    public List<GetAllOrdersByStatusResponse> getAllByTypeAndStatusInDay(OrderType orderType, OrderStatus orderStatus, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999);
        List<GetAllOrdersByStatusResponse> data = orderRepository.getAllByTypeAndStatusInDay(Date.from(startOfDay.toInstant()), Date.from(endOfDay.toInstant()), orderType, orderStatus, skip, limit);
        return data;
    }

    @Override
    public GetOrderDetailsResponse getOrderDetailsById(String id) {
        GetOrderDetailsResponse order = orderRepository.getOrderDetailsById(id);
        if (order == null) {
            throw new CustomException(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }

    @Override
    public List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String userId, OrderStatus orderStatus, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        List<GetAllOrderHistoryByUserIdResponse> order = orderRepository.getOrdersHistoryByUserIdAndOrderStatus(new ObjectId(userId), orderStatus, skip, limit);
        if (order == null) {
            throw new CustomException(ErrorConstant.NOT_FOUND + userId);
        }
        return order;
    }


    @Override
    public void createReviewForOrder(CreateReviewRequest body, String id) {
        ReviewEmbedded data = modelMapperUtils.mapClass(body, ReviewEmbedded.class);
        OrderCollection order = getByOrderId(id);
        if (order.getEventLogs().get(order.getEventLogs().size() - 1).getOrderStatus() != OrderStatus.SUCCEED) {
            throw new CustomException(ErrorConstant.ORDER_NOT_COMPLETED);
        }
        order.setReview(data);
        orderRepository.save(order);
    }

    @Override
    public List<GetOrderStatusLineResponse> getOrderEventLogById(String orderId) {
        OrderCollection order = orderRepository.findById(orderId).orElseThrow();
        List<GetOrderStatusLineResponse> resData = new ArrayList<GetOrderStatusLineResponse>();
        List<OrderLogEmbedded> logs = order.getEventLogs();
        for (OrderLogEmbedded log : logs) {
            resData.add(modelMapperUtils.mapClass(log, GetOrderStatusLineResponse.class));
        }
        return resData;
    }

    @Override
    public List<GetOrderHistoryPageForEmployeeResponse> getOrderHistoryPageForEmployee(OrderStatus orderStatus, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return orderRepository.getAllOrderHistoryForEmployee(orderStatus, skip, limit);
    }

    @Override
    public GetOrderQuantityByStatusResponse getOrderQuantityByStatusAtCurrentDate(OrderStatus orderStatus) {
        Date startDate = mongoDbUtils.createCurrentDateTime(0, 0, 0, 0);
        Date endDate = mongoDbUtils.createCurrentDateTime(23, 59, 59, 999);
        GetOrderQuantityByStatusResponse current = orderRepository.getOrderQuantityByStatus(orderStatus, startDate, endDate);
        if (current == null) {
            current = new GetOrderQuantityByStatusResponse();
            current.setOrderQuantity(0);
        }
        Date startDatePrev = mongoDbUtils.createPreviousDay(0, 0, 0, 0, 1);
        Date endDatePrev = mongoDbUtils.createPreviousDay(23, 59, 59, 999, 1);
        GetOrderQuantityByStatusResponse prev = orderRepository.getOrderQuantityByStatus(orderStatus, startDatePrev, endDatePrev);
        int difference = 0;
        if (prev != null) {
            difference = current.getOrderQuantity() - prev.getOrderQuantity();
        } else {
            difference = current.getOrderQuantity();
        }
        current.setDifference(difference);
        return current;
    }

    @Override
    public List<GetOrderHistoryPageForEmployeeResponse> searchOrderHistoryForEmployee(OrderStatus orderStatus, String key, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return orderRepository.searchOrderHistoryForEmployee(orderStatus, key, skip, limit);
    }

    public List<GetAllVisibleProductResponse> getTopProductQuantityOrder(int top) {
        return orderRepository.getTopProductQuantityOrder(top);
    }

    public void cancelOrderPreviousDay(OrderStatus orderStatus) {
        Date startDatePrev = mongoDbUtils.createPreviousDay(0, 0, 0, 0, 1);
        Date endDatePrev = mongoDbUtils.createPreviousDay(23, 59, 59, 999, 1);
        List<OrderCollection> orderList = orderRepository.getAllOrderByStatusLastAndTimeCreated(startDatePrev, endDatePrev, orderStatus);
        Iterator itr = orderList.iterator();

        while (itr.hasNext()) {
            OrderCollection order = (OrderCollection) itr.next();
            List<OrderLogEmbedded> orderStatusList = order.getEventLogs();
            orderStatusList.add(OrderLogEmbedded.builder()
                    .orderStatus(OrderStatus.CANCELED)
                    .description("Order automatically canceled")
                    .time(new Date())
                    .build());
            order.setEventLogs(orderStatusList);
            orderRepository.save(order);
        }
    }
}
