package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.collection.embedded.*;
import com.hcmute.drink.common.ToppingModel;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.CreateReviewRequest;
import com.hcmute.drink.dto.GetAllOrderHistoryByUserIdResponse;
import com.hcmute.drink.dto.GetAllShippingOrdersResponse;
import com.hcmute.drink.dto.GetOrderDetailsResponse;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import com.hcmute.drink.repository.OrderRepository;
import com.hcmute.drink.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SecurityUtils securityUtils;

    @Autowired
    @Lazy
    private TransactionServiceImpl transactionService;

    @Transactional
    public OrderCollection createShippingOrder(OrderCollection data, PaymentType paymentType) throws Exception {
        userService.exceptionIfNotExistedUserById(data.getUserId().toString());

        List<OrderDetailsEmbedded> products = data.getProducts();
        long totalPrice = 0;
        for (OrderDetailsEmbedded product : products) {
            ProductCollection productInfo = productService.findProductById(product.getProductId().toString()); //productRepository.findById(product.getProductId().toString()).orElse(null);
            if (productInfo == null) {
                throw new Exception(ErrorConstant.PRODUCT_NOT_FOUND + product.getProductId().toString());
            }
            double totalPriceToppings = 0;
            for (ToppingEmbedded topping : product.getToppings()) {
                totalPriceToppings += topping.getPrice();
            }
            totalPrice += totalPriceToppings;

            SizeEmbedded size = productInfo.getSizeList().stream().filter(it -> it.getSize().equals(product.getSize())).findFirst().orElseThrow();
            totalPrice += size.getPrice();

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
        OrderCollection order = findOrderByTransactionId(id);
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

    public OrderCollection findOrderByTransactionId(String id) throws Exception {
        return orderRepository.findById(id).orElseThrow(() -> new Exception(ErrorConstant.NOT_FOUND + id));
    }

    public OrderCollection findOrderById(String id) throws Exception {
        OrderCollection order = orderRepository.findByTransactionId(new ObjectId(id));
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

    public List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String userId, OrderStatus orderStatus) throws Exception {
        List<GetAllOrderHistoryByUserIdResponse> order = orderRepository.getOrdersHistoryByUserId(userId, orderStatus);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + userId);
        }
        return order;
    }

    public List<GetAllOrderHistoryByUserIdResponse> completeOrder(String userId, OrderStatus orderStatus) throws Exception {
        List<GetAllOrderHistoryByUserIdResponse> order = orderRepository.getOrdersHistoryByUserId(userId, orderStatus);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + userId);
        }
        return order;
    }

    public OrderCollection createReviewForOrder(String id, ReviewEmbedded data) throws Exception {
        OrderCollection order = findOrderByTransactionId(id);
        securityUtils.exceptionIfNotMe(order.getUserId().toString());
        if (order.getEventLogs().get(order.getEventLogs().size()).getOrderStatus() != OrderStatus.SUCCEED) {
            throw new Exception(ErrorConstant.ORDER_NOT_COMPLETED);
        }
        order.setReview(data);
        return orderRepository.save(order);
    }
}
