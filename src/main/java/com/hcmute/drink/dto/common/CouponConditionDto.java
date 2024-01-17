package com.hcmute.drink.dto.common;

import com.hcmute.drink.enums.ConditionOperator;
import com.hcmute.drink.enums.ConditionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.BOOLEAN_EX;

@Data
public class CouponConditionDto {
    @Schema(example = COUPON_CONDITION_DESCRIPTION_EX)
    @NotBlank
    private String description;

    @Schema(example = COUPON_CONDITION_TYPE_EX)
    @NotNull
    private ConditionType type;

    @Schema(example = BOOLEAN_EX)
    @NotNull
    private Object value;
}
