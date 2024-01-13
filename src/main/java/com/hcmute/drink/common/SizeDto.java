package com.hcmute.drink.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.PRODUCT_PRICE_MIN;

@Data
public class SizeDto {
    @Schema(example = PRODUCT_SIZE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String size;

    @Schema(example = PRODUCT_PRICE_EX, description = MIN_VALUE_DES + PRODUCT_PRICE_MIN)
    @Min(PRODUCT_PRICE_MIN)
    private double price;
}
