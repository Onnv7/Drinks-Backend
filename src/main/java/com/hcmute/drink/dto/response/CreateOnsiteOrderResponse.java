package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOnsiteOrderResponse {
    private String orderId;
    private String paymentUrl;
    private String transactionId;
}
