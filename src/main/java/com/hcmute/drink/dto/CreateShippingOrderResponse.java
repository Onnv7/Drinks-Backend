package com.hcmute.drink.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateShippingOrderResponse {
    private String orderId;
    private String paymentUrl;
    private String invoiceCode;
}

