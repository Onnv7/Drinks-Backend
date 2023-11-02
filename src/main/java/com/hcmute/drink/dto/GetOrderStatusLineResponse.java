package com.hcmute.drink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.OrderStatus;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class GetOrderStatusLineResponse {
    private OrderStatus orderStatus;
    private Date time;
    private String description;
    @JsonProperty("makerByEmployee")
    private boolean isEmployee;
}
