package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateBuyGetCouponRequest;
import com.hcmute.drink.dto.request.CreateCouponRequest;
import com.hcmute.drink.dto.request.UpdateMoneyCouponRequest;
import com.hcmute.drink.dto.request.UpdateProductGiftCouponRequest;
import com.hcmute.drink.dto.response.GetCouponDetailsByIdResponse;
import com.hcmute.drink.dto.response.GetCouponListResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponByIdResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponListResponse;
import com.hcmute.drink.enums.CouponType;

import java.util.List;

public interface ICouponService {
    void createMoneyCoupon(CreateCouponRequest body, CouponType couponType);
    void updateMoneyCoupon(UpdateMoneyCouponRequest body, String couponId, CouponType couponType);
    void updateProductGiftCoupon(UpdateProductGiftCouponRequest body, String couponId, CouponType couponType);
    void deleteCoupon(String couponId);
    List<GetReleaseCouponListResponse> getReleaseCouponList();
    List<GetReleaseCouponByIdResponse> getReleaseCouponById(String couponId);
    List<GetCouponListResponse> getCouponList();
    void createBuyGetCoupon(CreateBuyGetCouponRequest body);
    GetCouponDetailsByIdResponse getCouponById(String couponId);
}
