package com.hcmute.drink.collection.embedded;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;


@Data
public class OrderItemEmbedded {
    private ObjectId productId;

    private int quantity;
    private String size;

    private List<ToppingEmbedded> toppingList;

    private long price;

    private String note;
}
