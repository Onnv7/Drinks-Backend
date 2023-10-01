package com.hcmute.drink.common;

import com.hcmute.drink.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OrderLogModel {
    private OrderStatus orderStatus;
    private Date time;
}
