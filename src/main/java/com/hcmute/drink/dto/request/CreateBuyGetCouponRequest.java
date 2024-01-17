package com.hcmute.drink.dto.request;

import com.hcmute.drink.dto.common.CouponConditionDto;
import com.hcmute.drink.dto.common.ProductGiftDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.DATE_ISO_EX;

@Data
public class CreateBuyGetCouponRequest {
    @Schema(example = COUPON_CODE_EX)
    @NotBlank
    private String code;

    @Schema(example = COUPON_DESCRIPTION_EX)
    @NotBlank
    private String description;

    @Schema()
    @NotNull
    private ProductGiftDto productGift;

    @Schema()
    @NotEmpty
    private List<CouponConditionDto> conditionList;

    @Schema(example = BOOLEAN_EX)
    private boolean canMultiple;

    @Schema(example = DATE_ISO_EX)
    @NotNull
    private Date startDate;

    @Schema(example = DATE_ISO_EX)
    private Date expirationDate;
}
