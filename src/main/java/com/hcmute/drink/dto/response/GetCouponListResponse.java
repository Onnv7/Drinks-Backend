package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.CouponStatus;
import com.hcmute.drink.enums.CouponType;
import lombok.Data;

@Data
public class GetCouponListResponse {
    private String id;
    private String code;
    private CouponType couponType;
    private boolean isExpired;
    private CouponStatus status;
}
