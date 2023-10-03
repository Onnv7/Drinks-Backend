package com.hcmute.drink.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "address")
public class AddressCollection {
    private String details;
    private double longitude;
    private double latitude;
}
