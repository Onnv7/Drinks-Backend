package com.hcmute.drink.collection.embedded;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;


@Data
public class OrderDetailsEmbedded {
    private ObjectId productId;

    private int quantity;
    private String size;

    private List<ToppingEmbedded> toppings;


    private double price;

    private String note;
}
