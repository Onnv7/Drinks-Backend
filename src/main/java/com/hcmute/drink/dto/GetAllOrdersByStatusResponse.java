package com.hcmute.drink.dto;


import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetAllOrdersByStatusResponse {
    private String id;
    private double total;
    private List<String> productName;
    private OrderStatus statusLastEvent;
    private Date timeLastEvent;
}
