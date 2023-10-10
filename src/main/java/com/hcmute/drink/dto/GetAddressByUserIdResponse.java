package com.hcmute.drink.dto;

import lombok.Data;

@Data
public class GetAddressByUserIdResponse {
    private String id;
    private String details;
    private String recipientName;
    private String phoneNumber;
}
