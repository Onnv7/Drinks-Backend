package com.hcmute.drink.collection.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipientInfoEmbedded {
    private String details;
    private Double longitude;
    private Double latitude;
    private String note;
    private String recipientName;
    private String phoneNumber;
}
