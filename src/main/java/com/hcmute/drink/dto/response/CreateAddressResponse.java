package com.hcmute.drink.dto.response;

import lombok.Data;

@Data
public class CreateAddressResponse {
    private String id;
    private String details;
    private double longitude;
    private double latitude;
    private String note;
    private String recipientName;
    private boolean isDefault;
    private String phoneNumber;
}
