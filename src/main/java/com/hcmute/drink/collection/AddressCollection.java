package com.hcmute.drink.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AddressCollection {
    @Id
    private String id;
    private String details;
    private double longitude;
    private double latitude;
    private String note;
    private String recipientName;

    private String phoneNumber;
}
