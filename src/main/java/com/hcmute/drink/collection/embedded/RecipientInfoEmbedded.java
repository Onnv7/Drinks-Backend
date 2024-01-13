package com.hcmute.drink.collection.embedded;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipientInfoEmbedded {
    private String details;
    private Double longitude;
    private Double latitude;
    private String note;
    private String recipientName;
    private String phoneNumber;
}
