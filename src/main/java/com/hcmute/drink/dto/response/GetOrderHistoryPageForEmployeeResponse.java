package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import lombok.Data;

import java.util.Date;

@Data
public class GetOrderHistoryPageForEmployeeResponse {
    private String id;
    private String customerName;
    private String phoneNumber;
    private String productName;
    private int productQuantity;
    @JsonProperty("thumbnailUrl")
    private String productThumbnail;
    private Date timeLastEvent;
    private double total;
    private OrderStatus statusLastEvent;
    private OrderType orderType;
}
