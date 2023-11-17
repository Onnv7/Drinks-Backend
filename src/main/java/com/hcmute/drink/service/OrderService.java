package com.hcmute.drink.service;

import com.hcmute.drink.collection.*;
import com.hcmute.drink.collection.embedded.*;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.enums.*;
import com.hcmute.drink.payment.Config;
import com.hcmute.drink.payment.VNPayUtils;
import com.hcmute.drink.repository.OrderRepository;
import com.hcmute.drink.utils.MongoDbUtils;
import com.hcmute.drink.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.hcmute.drink.constant.VNPayConstant.*;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final SecurityUtils securityUtils;
    private final VNPayUtils vnPayUtils;
    private final ModelMapper modelMapper;
    private final MongoDbUtils mongoDbUtils;
    private final EmployeeService employeeService;

    @Autowired
    @Lazy
    private TransactionService transactionService;

    public OrderCollection exceptionIfNotExistedOrderByTransactionId(String transactionId) throws Exception {
        OrderCollection order = orderRepository.findByTransactionId(new ObjectId(transactionId));
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + " with transaction id " + transactionId);
        }
        return order;
    }

    public OrderCollection exceptionIfNotExistedOrderById(String id) throws Exception {
        OrderCollection order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + " with order id " + id);
        }
        return order;
    }
    // SERVICES =================================================================

    @Transactional
    public CreateShippingOrderResponse createShippingOrder(OrderCollection data, PaymentType paymentType, HttpServletRequest request) throws Exception {
        userService.exceptionIfNotExistedUserById(data.getUserId().toString());

        List<OrderDetailsEmbedded> products = data.getProducts();
        long totalPrice = 0;
        for (OrderDetailsEmbedded product : products) {
            ProductCollection productInfo = productService.exceptionIfNotExistedProductById(product.getProductId().toString()); //productRepository.findById(product.getProductId().toString()).orElse(null);

            double totalPriceToppings = 0;
            List<ToppingEmbedded> toppings = product.getToppings() != null ? product.getToppings() : new ArrayList<>();
            for (ToppingEmbedded topping : toppings) {
                totalPriceToppings += topping.getPrice();
            }
             ;

            SizeEmbedded size = productInfo.getSizeList().stream().filter(it -> it.getSize().equals(product.getSize())).findFirst().orElseThrow();
            totalPrice += (size.getPrice() + totalPriceToppings) * product.getQuantity();

        }
        data.setTotal(totalPrice);
        TransactionCollection transData = new TransactionCollection();
        CreateShippingOrderResponse resData = new CreateShippingOrderResponse();
        if (paymentType == PaymentType.CASHING) {

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(VNP_TIME_ZONE));
            SimpleDateFormat formatter = new SimpleDateFormat(VNP_TIME_FORMAT);
            String vnp_CreateDate = formatter.format(cld.getTime());

            transData = TransactionCollection.builder()
                    .invoiceCode(Config.getRandomNumber(8))
                    .timeCode(vnp_CreateDate)
                    .status(PaymentStatus.UNPAID)
                    .paymentType(PaymentType.CASHING).build();

        } else {
            Map<String, String> paymentData = vnPayUtils.createUrlPayment(request, totalPrice, "Shipping Order Info");

            transData = TransactionCollection.builder()
                    .invoiceCode(paymentData.get(VNP_TXN_REF_KEY).toString())
                    .timeCode(paymentData.get(VNP_CREATE_DATE_KEY))
                    .status(PaymentStatus.UNPAID)
                    .paymentType(paymentType)
                    .build();
            resData.setPaymentUrl(paymentData.get(VNP_URL_KEY));
        }


        TransactionCollection savedData = transactionService.createTransaction(transData);
        data.setOrderType(OrderType.SHIPPING);
        data.setTransactionId(new ObjectId(savedData.getId()));
        String makerId = securityUtils.getCurrentUserId();
        OrderLogEmbedded log = OrderLogEmbedded.builder()
                .orderStatus(OrderStatus.CREATED)
                .isEmployee(false)
                .makerId(new ObjectId(makerId))
                .time(new Date()).build();
        data.setEventLogs(new ArrayList<>(Arrays.<OrderLogEmbedded>asList(log)));
        OrderCollection order = orderRepository.save(data);

        resData.setOrderId(order.getId());
        resData.setTransactionId(transData.getId());

        return resData;
    }

    public void addNewOrderEvent(Maker maker, String id, OrderStatus orderStatus, String description) throws Exception {
        OrderCollection order = exceptionIfNotExistedOrderById(id);
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

    public List<GetAllShippingOrdersResponse> getAllShippingOrdersQueueForEmployee() throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999);
        List<GetAllShippingOrdersResponse> ordersCreatedOnDate = orderRepository.getAllShippingOrdersQueueForEmployee(Date.from(startOfDay.toInstant()), Date.from(endOfDay.toInstant()));

        return ordersCreatedOnDate;
    }

    public List<GetAllOrdersByStatusResponse> getAllByTypeAndStatusInDay(OrderType orderType, OrderStatus orderStatus, int page, int size) throws Exception {
        int skip = (page - 1) * size;
        int limit = size;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999);
        List<GetAllOrdersByStatusResponse> data = orderRepository.getAllByTypeAndStatusInDay(Date.from(startOfDay.toInstant()), Date.from(endOfDay.toInstant()), orderType, orderStatus, skip, limit);
        return data;
    }




    public GetOrderDetailsResponse getOrderDetailsById(String id) throws Exception {
        GetOrderDetailsResponse order = orderRepository.getOrderDetailsById(id);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }

    public List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String userId, OrderStatus orderStatus, int page, int size) throws Exception {
        int skip = (page - 1) * size;
        int limit = size;
        List<GetAllOrderHistoryByUserIdResponse> order = orderRepository.getOrdersHistoryByUserIdAndOrderStatus(new ObjectId(userId), orderStatus, skip, limit);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + userId);
        }
        return order;
    }



    public OrderCollection createReviewForOrder(String id, ReviewEmbedded data) throws Exception {
        OrderCollection order = exceptionIfNotExistedOrderById(id);
        securityUtils.exceptionIfNotMe(order.getUserId().toString());
        if (order.getEventLogs().get(order.getEventLogs().size() -1).getOrderStatus() != OrderStatus.SUCCEED) {
            throw new Exception(ErrorConstant.ORDER_NOT_COMPLETED);
        }
        order.setReview(data);
        return orderRepository.save(order);
    }

    public List<GetOrderStatusLineResponse> getOrderEventLogById(String orderId) throws Exception {
        OrderCollection order = orderRepository.findById(orderId).orElseThrow();
        List<GetOrderStatusLineResponse> resData = new ArrayList<GetOrderStatusLineResponse>();
        List<OrderLogEmbedded> logs = order.getEventLogs();
        for (OrderLogEmbedded log : logs) {
            resData.add(modelMapper.map(log, GetOrderStatusLineResponse.class));
        }
        return resData;
    }

    public List<GetOrderHistoryPageForEmployeeResponse> getOrderHistoryPageForEmployee(OrderStatus orderStatus, int page, int size)  {
        int skip = (page - 1) * size;
        int limit = size;
        return orderRepository.getAllOrderHistoryForEmployee(orderStatus, skip, limit);
    }



    public GetOrderQuantityByStatusResponse getOrderQuantityByStatusAtCurrentDate(OrderStatus orderStatus)  {
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
        if(prev != null) {
            difference = current.getOrderQuantity() - prev.getOrderQuantity();
        } else {
            difference = current.getOrderQuantity();
        }
        current.setDifference(difference);
        return current;
    }
    public List<GetOrderHistoryPageForEmployeeResponse> searchOrderHistoryForEmployee(OrderStatus orderStatus, String key, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return orderRepository.searchOrderHistoryForEmployee(orderStatus, key, skip, limit);
    }

    public List<GetAllProductsEnabledResponse> getTopProductQuantityOrder(int top) {
        return orderRepository.getTopProductQuantityOrder(top);
    }
}
