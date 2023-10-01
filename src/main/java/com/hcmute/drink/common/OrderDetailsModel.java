package com.hcmute.drink.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.ORDER_QUANTITY_MIN;

@Data
public class OrderDetailsModel {
    @NotNull
    private ObjectId productId;

    @Min(ORDER_QUANTITY_MIN)
    private int quantity;
    private List<ToppingModel> toppings;
    private double price;
    private String note;
}
