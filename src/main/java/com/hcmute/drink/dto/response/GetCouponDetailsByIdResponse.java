package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hcmute.drink.dto.common.ProductGiftDto;
import com.hcmute.drink.enums.ConditionType;
import com.hcmute.drink.enums.CouponStatus;
import com.hcmute.drink.enums.CouponType;
import com.hcmute.drink.enums.DiscountUnitType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCouponDetailsByIdResponse {
    private String id;
    private String code;
    private CouponType couponType;
    private String description;
    private ProductGift productGift;
    private MoneyDiscount moneyDiscount;
    private CouponStatus status;
    private List<Condition> conditionList;
    private Date startDate;
    private Date expirationDate;
    private Boolean canMultiple;


    @Data
    static class MoneyDiscount {
        private DiscountUnitType unit;
        private Long value;
    }

    @Data
    static class ProductGift {
        private String productId;
        private String size;
        private Integer quantity;
    }

    @Data
    public static class Condition {
        private String description;
        private ConditionType type;
        private Object value;
    }

}
