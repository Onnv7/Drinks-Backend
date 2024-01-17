package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateBuyGetCouponRequest;
import com.hcmute.drink.dto.request.CreateCouponRequest;
import com.hcmute.drink.dto.request.UpdateCouponRequest;
import com.hcmute.drink.dto.response.GetReleaseCouponByIdResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponListResponse;
import com.hcmute.drink.enums.CouponType;

import java.util.List;

public interface ICouponService {
    void createMoneyCoupon(CreateCouponRequest body, CouponType couponType);
    void updateCoupon(UpdateCouponRequest body, String couponId);
    void deleteCoupon(String couponId);
    List<GetReleaseCouponListResponse> getReleaseCouponList();
    List<GetReleaseCouponByIdResponse> getReleaseCouponById(String couponId);
    void createBuyGetCoupon(CreateBuyGetCouponRequest body);
}
