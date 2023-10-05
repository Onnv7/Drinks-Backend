package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.common.ToppingModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class OrderDetailsEmbedded {
    private ObjectId productId;

    private int quantity;

    private List<ToppingEmbedded> toppings;


    private double price;

    private String note;
}
