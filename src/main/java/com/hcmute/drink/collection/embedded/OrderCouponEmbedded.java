package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCouponEmbedded {
    private String code;
    private CouponType couponType;
    private ObjectId productTarget;
    private Object discount;
}
