package com.hcmute.drink.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class ProductGiftDto {
    @Schema(example = OBJECT_ID_EX)
    @NotNull
    private ObjectId productId;

    @Schema(example = PRODUCT_SIZE_EX)
    @NotBlank
    private String size;

    @Schema(example = PRODUCT_QUANTITY_EX)
    @NotNull
    private Integer quantity;
}
