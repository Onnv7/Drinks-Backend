package com.hcmute.drink.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class OrderItemDto {
    @Schema(example = OBJECT_ID_EX, description = NOT_NULL_DES)
    @NotNull
    private ObjectId productId;

    @Schema(example = PRODUCT_QUANTITY_EX, description = MIN_LENGTH_DES + ORDER_QUANTITY_MIN)
    @Min(ORDER_QUANTITY_MIN)
    private int quantity;

    @Schema(description = OPTIONAL_DES)
    private List<ToppingDto> toppingList;


    @Schema(example = PRODUCT_PRICE_EX, description = MIN_VALUE_DES + PRODUCT_PRICE_MIN)
    @Min(PRODUCT_PRICE_MIN)
    private double price;

    @Schema(example = OBJECT_ID_EX, description = NOT_NULL_DES)
    @NotNull
    private String size;

    @Schema(example = NOT_EMPTY_DES, description = PRODUCT_NOTE_EX)
    private String note;
}
