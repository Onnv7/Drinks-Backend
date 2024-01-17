package com.hcmute.drink.dto.common;

import com.hcmute.drink.enums.DiscountUnitType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class MoneyDiscountDto {
    @Schema(example = COUPON_UNIT_EX)
    @NotNull
    private DiscountUnitType unit;

//    @Schema(example = DISCOUNT_VALUE_EX)
//    private Long moneyDiscount;
//
//    @Schema(example = OBJECT_ID_EX)
//    private ObjectId productDiscount;

    @Schema(example = DISCOUNT_VALUE_EX)
    private Long value;
}
