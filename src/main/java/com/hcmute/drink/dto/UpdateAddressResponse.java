package com.hcmute.drink.dto;

import lombok.Data;

@Data
public class UpdateAddressResponse {

    private String id;
    private String details;
    private double longitude;
    private double latitude;
    private String note;
    private String recipientName;

    private String phoneNumber;
}
