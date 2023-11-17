package com.hcmute.drink.controller;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.embedded.ReviewEmbedded;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.enums.Maker;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.payment.VNPayUtils;
import com.hcmute.drink.service.OrderService;
import com.hcmute.drink.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = ORDER_CONTROLLER_TITLE)
@RestController
@RequiredArgsConstructor
@RequestMapping(ORDER_BASE_PATH)
@Validated
public class OrderController {
    private final ModelMapper modelMapper;
    private final VNPayUtils vnPayUtils;
    private final OrderService orderService;
    private final TransactionService transactionService;


    @Operation(summary = ORDER_CREATE_SHIPPING_SUM, description = ORDER_CREATE_SHIPPING_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = ORDER_CREATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> createShippingOrder(HttpServletRequest request, @RequestBody @Validated CreateOrderRequest body) {
        try {
            OrderCollection data = modelMapper.map(body, OrderCollection.class);
            CreateShippingOrderResponse  resData =  orderService.createShippingOrder(data, body.getPaymentType(), request);
              ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resData)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Operation(summary = ORDER_GET_ALL_ORDER_HISTORY_FOR_EMPLOYEE_SUM, description = ORDER_GET_ALL_ORDER_HISTORY_FOR_EMPLOYEE_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ORDER_GET_ALL_ORDER_HISTORY_FOR_EMPLOYEE_SUB_PATH)
    public ResponseEntity<ResponseAPI> getOrderHistoryPageForEmployee(
            @PathVariable("orderStatus") OrderStatus orderStatus,
            @Parameter(name = "key", description = "Key is order's id, customer name or phone number", required = false, example = "65439a55e9818f43f8b8e02c")
            @RequestParam(name = "key", required = false) String key,
            @Parameter(name = "page", required = true, example = "1")
            @RequestParam("page")   @Min(value = 1, message = "Page must be greater than 0") int page,
            @Parameter(name = "size", required = true, example = "10")
            @RequestParam("size") @Min(value = 1, message = "Size must be greater than 0")  int size) {
        try {

            List<GetOrderHistoryPageForEmployeeResponse> resData = new ArrayList<>();
            if(key == null) {
                resData =  orderService.getOrderHistoryPageForEmployee(orderStatus, page, size);
            } else {
                resData =  orderService.searchOrderHistoryForEmployee(orderStatus, key, page, size);
            }

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resData)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ORDER_UPDATE_EVENT_SUM, description = ORDER_UPDATE_EVENT_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PatchMapping(path = ORDER_UPDATE_STATUS_SUB_PATH)
    // nhân viên cập nhật trạng thái order vào event logs
    public ResponseEntity<ResponseAPI> addNewOrderEvent(@PathVariable("maker") Maker maker, @PathVariable("orderId") String id, @RequestBody @Validated UpdateOrderStatusRequest body) {
        try {
            orderService.addNewOrderEvent(maker, id, body.getOrderStatus(), body.getDescription());
//            UpdateOrderStatusResponse resData = modelMapper.map(savedData, UpdateOrderStatusResponse.class);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ORDER_GET_ALL_IN_DAY_SUM, description = ORDER_GET_ALL_IN_DAY_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ORDER_GET_ALL_SHIPPING_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllShippingOrdersQueueForEmployee() {
        try {
            List<GetAllShippingOrdersResponse> savedData =  orderService.getAllShippingOrdersQueueForEmployee();
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(savedData)
                    .message(SuccessConstant.GET)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ORDER_GET_ALL_BY_TYPE_AND_STATUS_IN_DAY_SUM, description = ORDER_GET_ALL_BY_TYPE_AND_STATUS_IN_DAY_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ORDER_GET_ALL_BY_STATUS_AND_TYPE_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllByTypeAndStatusInDay(
            @PathVariable("orderType") OrderType orderType,
            @PathVariable("orderStatus") OrderStatus orderStatus,
            @Parameter(name = "page", required = true, example = "1")
            @RequestParam("page")   @Min(value = 1, message = "Page must be greater than 0") int page,
            @Parameter(name = "size", required = true, example = "10")
            @RequestParam("size") @Min(value = 1, message = "Size must be greater than 0")  int size
    ) {
        try {
            List<GetAllOrdersByStatusResponse> dataRes =  orderService.getAllByTypeAndStatusInDay(orderType, orderStatus, page, size);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(dataRes)
                    .message(SuccessConstant.GET)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ORDER_GET_DETAILS_BY_ID_SUM, description = ORDER_GET_DETAILS_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ORDER_GET_DETAILS_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getDetailsOrder(@PathVariable("orderId") String id) {
        try {
            GetOrderDetailsResponse savedData =  orderService.getOrderDetailsById(id);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(savedData)
                    .message(SuccessConstant.GET)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_SUM, description = ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_SUB_PATH)
    public ResponseEntity<ResponseAPI> getOrdersHistoryByUserIdAndOrderStatus(
            @PathVariable("userId") String id,
            @RequestParam("orderStatus") OrderStatus orderStatus,
            @Parameter(name = "page", required = true, example = "1")
            @Min(value = 1, message = "Page must be greater than 0")
            @RequestParam("page") int page,
            @Parameter(name = "size", required = true, example = "10")
            @RequestParam("size") @Min(value = 1, message = "Size must be greater than 0")  int size
    ) {
        try {
            List<GetAllOrderHistoryByUserIdResponse> savedData =  orderService.getOrdersHistoryByUserId(id, orderStatus, page, size);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(savedData)
                    .message(SuccessConstant.GET)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ORDER_CREATE_REVIEW_SUM, description = ORDER_CREATE_REVIEW_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = ORDER_CREATE_REVIEW_SUB_PATH)
    public ResponseEntity<ResponseAPI> createReviewForOrder(@PathVariable("orderId") String id, @RequestBody CreateReviewRequest body,  Principal principal) {
        try {
            ReviewEmbedded review = modelMapper.map(body, ReviewEmbedded.class);

            OrderCollection savedData =  orderService.createReviewForOrder(id, review);
            // TODO: không cần response
            CreateReviewOrderResponse resData = modelMapper.map(savedData, CreateReviewOrderResponse.class);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
//                    .data(resData)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = ORDER_GET_STATUS_LINE_SUM, description = ORDER_GET_STATUS_LINE_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ORDER_GET_STATUS_LINE_SUB_PATH)
    public ResponseEntity<ResponseAPI> getOrderStatusLine(@PathVariable("orderId") String orderId) {
        try {

            List<GetOrderStatusLineResponse> resData =  orderService.getOrderEventLogById(orderId);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resData)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Operation(summary = ORDER_GET_ORDER_QUANTITY_BY_STATUS_SUM, description = ORDER_GET_ORDER_QUANTITY_BY_STATUS_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = ORDER_GET_ORDER_QUANTITY_BY_STATUS_SUB_PATH)
    public ResponseEntity<ResponseAPI> getOrderQuantityByStatusAtCurrentDate(@RequestParam("status") OrderStatus orderStatus) {
        try {

            GetOrderQuantityByStatusResponse resData =  orderService.getOrderQuantityByStatusAtCurrentDate(orderStatus);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resData)
                    .message(SuccessConstant.GET)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
