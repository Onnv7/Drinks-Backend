package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.enums.DiscountUnitType;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;


@Data
public class OrderItemEmbedded {
    private ObjectId productId;

    private int quantity;
    private String size;

    private List<ToppingEmbedded> toppingList;
    private Long price;
    private String note;
    private String couponProductCode;

    private Long moneyDiscount;
    private ProductGiftEmbedded productGift;
}
