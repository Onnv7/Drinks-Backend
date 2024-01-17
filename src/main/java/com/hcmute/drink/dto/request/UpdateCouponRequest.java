package com.hcmute.drink.dto.request;

import com.hcmute.drink.dto.common.CouponConditionDto;
import com.hcmute.drink.dto.common.MoneyDiscountDto;
import com.hcmute.drink.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateCouponRequest {

    @Schema(example = COUPON_CODE_EX)
    @NotBlank
    private String code;

    @Schema(example = COUPON_DESCRIPTION_EX)
    @NotBlank
    private String description;

    @Schema(example = DISCOUNT_TARGET_EX)
    @NotNull
    private CouponType couponType;

    @Schema(example = COUPON_STATUS_EX)
    @NotNull
    private CouponStatus status;

    @Schema()
    @NotNull
    private List<MoneyDiscountDto> discount;

    // TODO: xem lại not null và not empty
    @Schema()
    @NotNull
    private List<CouponConditionDto> conditionList;

    @Schema(example = DATE_ISO_EX)
    @NotNull
    private Date startDate;

    @Schema(example = DATE_ISO_EX)
    private Date expirationDate;

}
