package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateOrderRequest;
import com.hcmute.drink.dto.request.CreateReviewRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.Maker;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IOrderService {
    CreateShippingOrderResponse createShippingOrder(CreateOrderRequest body, HttpServletRequest request);
    List<GetOrderHistoryPageForEmployeeResponse> getOrderHistoryPageForEmployee(OrderStatus orderStatus, int page, int size);
    List<GetOrderHistoryPageForEmployeeResponse> searchOrderHistoryForEmployee(OrderStatus orderStatus, String key, int page, int size);
    void addNewOrderEvent(Maker maker, String id, OrderStatus orderStatus, String description);
    List<GetAllShippingOrdersResponse> getAllShippingOrdersQueueForEmployee();
    List<GetAllOrdersByStatusResponse> getAllByTypeAndStatusInDay(OrderType orderType, OrderStatus orderStatus, int page, int size);
    GetOrderDetailsResponse getOrderDetailsById(String id);
    List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String userId, OrderStatus orderStatus, int page, int size);
    void createReviewForOrder(CreateReviewRequest body, String id);
    List<GetOrderStatusLineResponse> getOrderEventLogById(String orderId);
    GetOrderQuantityByStatusResponse getOrderQuantityByStatusAtCurrentDate(OrderStatus orderStatus);
}
