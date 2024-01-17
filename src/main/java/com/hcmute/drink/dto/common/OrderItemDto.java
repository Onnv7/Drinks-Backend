package com.hcmute.drink.dto.common;

import com.hcmute.drink.enums.DiscountUnitType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class OrderItemDto {
    @Schema(example = OBJECT_ID_EX)
    @NotNull
    private ObjectId productId;

    @Schema(example = PRODUCT_QUANTITY_EX)
    @Min(ORDER_QUANTITY_MIN)
    private Integer quantity;

    @Schema(description = OPTIONAL_DES)
    private List<String> toppingNameList;


//    @Schema(example = PRODUCT_PRICE_EX)
//    @Min(PRODUCT_PRICE_MIN)
//    private Long price;

    @Schema(example = OBJECT_ID_EX)
    @NotNull
    private String size;

    @Schema(example = NOT_EMPTY_DES)
    private String note;

    @Schema(example = COUPON_CODE_EX)
    private String couponProductCode;
}
