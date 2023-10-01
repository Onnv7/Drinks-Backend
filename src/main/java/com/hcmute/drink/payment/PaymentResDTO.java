package com.hcmute.drink.payment;

import lombok.Data;

@Data
public class PaymentResDTO {
    private String status;
    private String message;
    private String URL;
}
