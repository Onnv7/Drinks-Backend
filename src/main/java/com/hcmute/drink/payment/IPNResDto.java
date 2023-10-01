package com.hcmute.drink.payment;

import lombok.Data;

@Data
public class IPNResDto {
    private String RspCode;
    private String Message;
}
