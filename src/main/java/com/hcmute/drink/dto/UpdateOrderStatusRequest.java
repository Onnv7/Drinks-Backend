package com.hcmute.drink.dto;

import com.hcmute.drink.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus orderStatus;
}
