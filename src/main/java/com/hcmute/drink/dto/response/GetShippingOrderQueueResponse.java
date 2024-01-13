package com.hcmute.drink.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
public class GetShippingOrderQueueResponse {
    private String id;
    private String phoneNumber;
    private String productName;
    private int productQuantity;
    private String customerName;
    @JsonProperty("thumbnailUrl")
    private String productThumbnail;
    private double total;
    private OrderStatus statusLastEvent;
    private Date timeLastEvent;
}
