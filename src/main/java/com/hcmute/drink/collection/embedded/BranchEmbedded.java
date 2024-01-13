package com.hcmute.drink.collection.embedded;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class BranchEmbedded {
    private ObjectId id;
    private String address;
    private double longitude;
    private double latitude;
}
