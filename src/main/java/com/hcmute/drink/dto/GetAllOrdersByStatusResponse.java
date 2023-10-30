package com.hcmute.drink.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetAllOrdersByStatusResponse {
    private String id;
    private String phoneNumber;
    private int productQuantity;
    private String customerName;
    @JsonProperty("thumbnailUrl")
    private String productThumbnail;
    private double total;
    private OrderStatus statusLastEvent;
    private Date timeLastEvent;
}
