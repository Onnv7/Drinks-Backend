package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.collection.embedded.OrderDetailsEmbedded;
import com.hcmute.drink.collection.embedded.OrderLogEmbedded;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.GetAllShippingOrdersResponse;
import com.hcmute.drink.dto.GetOrderDetailsResponse;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import com.hcmute.drink.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;

    @Autowired
    @Lazy
    private TransactionServiceImpl transactionService;

    public OrderCollection createShippingOrder(OrderCollection data, PaymentType paymentType) throws Exception {
        userService.exceptionIfNotExistedUserById(data.getUserId().toString());

        List<OrderDetailsEmbedded> products = data.getProducts();
        long totalPrice = 0;
        for (OrderDetailsEmbedded product : products) {
            ProductCollection productInfo = productService.getProductById(product.getProductId().toString()); //productRepository.findById(product.getProductId().toString()).orElse(null);
            if (productInfo == null) {
                throw new Exception(ErrorConstant.PRODUCT_NOT_FOUND + product.getProductId().toString());
            }
            totalPrice += productInfo.getPrice();
        }
        data.setTotal(totalPrice);


        TransactionCollection transData = TransactionCollection.builder()
                .status(PaymentStatus.UNPAID)
                .paymentType(paymentType)
                .build();
        TransactionCollection savedData = transactionService.createTransaction(transData);

        data.setOrderType(OrderType.SHIPPING);
        data.setTransactionId(new ObjectId(savedData.getId()));

        OrderLogEmbedded log = OrderLogEmbedded.builder()
                .orderStatus(OrderStatus.CREATED)
                .time(new Date()).build();
        data.setEventLogs(new ArrayList<>(Arrays.<OrderLogEmbedded>asList(log)));
        OrderCollection order = orderRepository.save(data);
        return order;
    }

    public OrderCollection updateOrderEvent(String id, OrderStatus orderStatus, String description) throws Exception {
        OrderCollection order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        order.getEventLogs().add(OrderLogEmbedded.builder()
                .orderStatus(orderStatus)
                .description(description)
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

    public OrderCollection getOrderInfoByTransId(String id) throws Exception {
        OrderCollection order = orderRepository.findByTransactionId(id);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }

    public GetOrderDetailsResponse getOrderDetailsById(String id) throws Exception {
        GetOrderDetailsResponse order = orderRepository.getOrderDetailsById(id);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }
}
