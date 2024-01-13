package com.hcmute.drink.common;

import com.hcmute.drink.enums.OrderStatus;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class OrderEventDto {
    private OrderStatus orderStatus;
    private Date time;
    private String description;
    private ObjectId makerId;
    private boolean isEmployee;
}
