package com.hcmute.drink.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class ToppingModel {
    @Schema(example = TOPPING_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String name;

    @Schema(example = TOPPING_PRICE_EX, description = MIN_VALUE_DES + TOPPING_PRICE_MIN)
    @Min(TOPPING_PRICE_MIN)
    private double price;
}
