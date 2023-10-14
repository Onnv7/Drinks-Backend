package com.hcmute.drink.common;

import com.hcmute.drink.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class OrderLogModel {
    private OrderStatus orderStatus;
    private Date time;
    private String description;
}
