package com.hcmute.drink.dto;

import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import lombok.Data;

import java.util.Date;

@Data
public class GetAllOrderHistoryByUserIdResponse {
    private String id;
    private double total;
    private OrderType orderType;
    private String productName;
    private String thumbnailUrl;
    private OrderStatus orderStatus;
    private Date createdAt;
    private Date updatedAt;


}
