package com.hcmute.drink.collection.embedded;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ProductGiftEmbedded {
    private ObjectId productId;
    private String productName;
    private String size;
    private Integer quantity;
}
