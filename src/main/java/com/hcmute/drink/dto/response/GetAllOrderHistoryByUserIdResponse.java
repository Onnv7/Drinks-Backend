package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetAllOrderHistoryByUserIdResponse {
    private String id;
    private String code;
    private double total;
    private int productQuantity;
    private OrderType orderType;
    private String productName;
    private OrderStatus statusLastEvent;
    private Date timeLastEvent;


}
