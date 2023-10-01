package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.common.OrderLogModel;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import com.hcmute.drink.repository.OrderRepository;
import com.hcmute.drink.repository.ProductRepository;
import com.hcmute.drink.repository.UserRepository;
import com.hcmute.drink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final TransactionServiceImpl transactionService;

    public OrderCollection createOrder(OrderCollection data, PaymentType paymentType) throws Exception {
        userService.checkExistenceUserById(data.getUserId().toString());

        List<OrderDetailsModel> products = data.getProducts();
        double totalPrice = 0;
        for (OrderDetailsModel product : products) {
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

        data.setTransactionId(new ObjectId(savedData.getId()));

        OrderLogModel log = OrderLogModel.builder()
                .orderStatus(OrderStatus.CREATED)
                .time(new Date()).build();
        data.setEventLogs(new ArrayList<>(Arrays.<OrderLogModel>asList(log)));
        OrderCollection order = orderRepository.save(data);
        if (data == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }

        return order;
    }

    public OrderCollection updateOrderStatus(String id, OrderStatus orderStatus) throws Exception {
        OrderCollection order = orderRepository.findById(id).orElse(null);
        if(order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        order.getEventLogs().add(OrderLogModel.builder()
                .orderStatus(orderStatus)
                .time(new Date())
                .build()
        );
        OrderCollection updatedOrder = orderRepository.save(order);
        if(updatedOrder == null) {
            throw new Exception(ErrorConstant.UPDATE_FAILED);
        }
        return updatedOrder;
    }

    public OrderCollection getOrderInfoByTransId(String id) throws Exception {
        OrderCollection order = orderRepository.findByTransactionId(id);
        if (order == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }
}
