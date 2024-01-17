package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateOnsiteOrderRequest;
import com.hcmute.drink.dto.request.CreateShippingOrderRequest;
import com.hcmute.drink.dto.request.CreateReviewRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.Maker;
import com.hcmute.drink.enums.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IOrderService {
    CreateOrderResponse createShippingOrder(CreateShippingOrderRequest body, HttpServletRequest request);
    CreateOrderResponse createOnsiteOrder(CreateOnsiteOrderRequest body, HttpServletRequest request);
    List<GetOrderHistoryForEmployeeResponse> getOrderHistoryPageForEmployee(OrderStatus orderStatus, int page, int size);
    List<GetOrderHistoryForEmployeeResponse> searchOrderHistoryForEmployee(OrderStatus orderStatus, String key, int page, int size);
    void addNewOrderEvent(Maker maker, String id, OrderStatus orderStatus, String description);
    List<GetShippingOrderQueueResponse> getShippingOrderQueueToday(OrderStatus orderStatus, int page, int size);
    List<GetOnsiteOrderQueueResponse> getOnsiteOrderQueueToday(OrderStatus orderStatus, int page, int size);
    GetOrderDetailsResponse getOrderDetailsById(String id);
    List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String userId, OrderStatus orderStatus, int page, int size);
    void createReviewForOrder(CreateReviewRequest body, String id);
    List<GetOrderStatusLineResponse> getOrderEventLogById(String orderId);
    GetOrderQuantityByStatusResponse getOrderQuantityByStatusAtCurrentDate(OrderStatus orderStatus);
}
