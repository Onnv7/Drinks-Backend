package com.hcmute.drink.dto.response;

import lombok.Data;

@Data
public class GetAddressByUserIdResponse {
    private String id;
    private String details;
    private String recipientName;
    private boolean isDefault;
    private String phoneNumber;
}
