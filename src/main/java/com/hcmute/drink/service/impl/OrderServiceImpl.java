package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.collection.embedded.*;
import com.hcmute.drink.common.OrderLogModel;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import com.hcmute.drink.payment.Config;
import com.hcmute.drink.payment.VNPayUtils;
import com.hcmute.drink.repository.OrderRepository;
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


@Service
@RequiredArgsConstructor
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final SecurityUtils securityUtils;
    private final VNPayUtils vnPayUtils;
    private final ModelMapper modelMapper;

    @Autowired
    @Lazy
    private TransactionServiceImpl transactionService;

    @Transactional
    public CreateShippingOrderResponse createShippingOrder(OrderCollection data, PaymentType paymentType, HttpServletRequest request) throws Exception {
        userService.exceptionIfNotExistedUserById(data.getUserId().toString());

        List<OrderDetailsEmbedded> products = data.getProducts();
        long totalPrice = 0;
        for (OrderDetailsEmbedded product : products) {
            ProductCollection productInfo = productService.findProductById(product.getProductId().toString()); //productRepository.findById(product.getProductId().toString()).orElse(null);
            if (productInfo == null) {
                throw new Exception(ErrorConstant.PRODUCT_NOT_FOUND + product.getProductId().toString());
            }
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

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            transData = TransactionCollection.builder()
                    .invoiceCode(Config.getRandomNumber(8))
                    .timeCode(vnp_CreateDate)
                    .status(PaymentStatus.UNPAID)
                    .paymentType(PaymentType.CASHING).build();

        } else {
            Map<String, String> paymentData = vnPayUtils.createUrlPayment(request, totalPrice, "Shipping Order Info");

            transData = TransactionCollection.builder()
                    .invoiceCode(paymentData.get("vnp_TxnRef").toString())
                    .timeCode(paymentData.get("vnp_CreateDate"))
                    .status(PaymentStatus.UNPAID)
                    .paymentType(paymentType)
                    .build();
            resData.setPaymentUrl(paymentData.get("vnp_url"));
        }


        TransactionCollection savedData = transactionService.createTransaction(transData);
        data.setOrderType(OrderType.SHIPPING);
        data.setTransactionId(new ObjectId(savedData.getId()));

        OrderLogEmbedded log = OrderLogEmbedded.builder()
                .orderStatus(OrderStatus.CREATED)
                .time(new Date()).build();
        data.setEventLogs(new ArrayList<>(Arrays.<OrderLogEmbedded>asList(log)));
        OrderCollection order = orderRepository.save(data);

        resData.setOrderId(order.getId());
        resData.setTransactionId(transData.getId());

        return resData;
    }

    public OrderCollection updateOrderEvent(String id, OrderStatus orderStatus, String description) throws Exception {
        OrderCollection order = findOrderById(id);
        String employeeId = securityUtils.getCurrentUserId();
        order.getEventLogs().add(OrderLogEmbedded.builder()
                .orderStatus(orderStatus)
                .description(description)
                .employeeId(new ObjectId(employeeId))
                .time(new Date())
                .build()
        );
        OrderCollection updatedOrder = orderRepository.save(order);
        if (updatedOrder == null) {
            throw new Exception(ErrorConstant.UPDATE_FAILED);
        }
        return updatedOrder;
    }

    public List<GetAllShippingOrdersResponse> getAllShippingOrdersQueueForEmployee() throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999);
        List<GetAllShippingOrdersResponse> ordersCreatedOnDate = orderRepository.getAllShippingOrdersQueueForEmployee(Date.from(startOfDay.toInstant()), Date.from(endOfDay.toInstant()));

        return ordersCreatedOnDate;
    }

    public List<GetAllOrdersByStatusResponse> getAllByTypeAndStatusInDay(OrderType orderType, OrderStatus orderStatus) throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999);
        List<GetAllOrdersByStatusResponse> data = orderRepository.getAllByTypeAndStatusInDay(Date.from(startOfDay.toInstant()), Date.from(endOfDay.toInstant()), orderType, orderStatus);
        return data;
    }

    public OrderCollection findOrderByTransactionId(String id) throws Exception {
        OrderCollection order = orderRepository.findByTransactionId(new ObjectId(id));
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }

    public OrderCollection findOrderById(String id) throws Exception {
        return orderRepository.findById(id).orElseThrow();
    }

    public GetOrderDetailsResponse getOrderDetailsById(String id) throws Exception {
        GetOrderDetailsResponse order = orderRepository.getOrderDetailsById(id);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }

    public List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String userId, OrderStatus orderStatus) throws Exception {
        List<GetAllOrderHistoryByUserIdResponse> order = orderRepository.getOrdersHistoryByUserId(new ObjectId(userId), orderStatus);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + userId);
        }
        return order;
    }



    public OrderCollection createReviewForOrder(String id, ReviewEmbedded data) throws Exception {
        OrderCollection order = findOrderById(id);
        securityUtils.exceptionIfNotMe(order.getUserId().toString());
        if (order.getEventLogs().get(order.getEventLogs().size() -1).getOrderStatus() != OrderStatus.SUCCEED) {
            throw new Exception(ErrorConstant.ORDER_NOT_COMPLETED);
        }
        order.setReview(data);
        return orderRepository.save(order);
    }

    public List<OrderLogModel> getOrderEventLogById(String orderId) throws Exception {
        OrderCollection order = orderRepository.findById(orderId).orElseThrow();
        List<OrderLogModel> resData = new ArrayList<OrderLogModel>();
        List<OrderLogEmbedded> logs = order.getEventLogs();
        for (OrderLogEmbedded log : logs) {
            resData.add(modelMapper.map(log, OrderLogModel.class));
        }
        return resData;
    }

    public List<GetOrderHistoryPageForEmployeeResponse> getOrderHistoryPageForEmployee(int page, int size)  {

        int skip = (page - 1) * size;
        int limit = size;
        List<GetOrderHistoryPageForEmployeeResponse> list =  orderRepository.getAllOrderHistoryForEmployee(skip, limit);
        return list;
    }
}
