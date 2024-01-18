package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.request.*;
import com.hcmute.drink.dto.response.GetCouponDetailsByIdResponse;
import com.hcmute.drink.dto.response.GetCouponListResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponByIdResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponListResponse;
import com.hcmute.drink.enums.CouponType;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.ICouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = COUPON_CONTROLLER_TITLE)
@RestController
@RequestMapping(COUPON_BASE_PATH)
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;

    @Operation(summary = COUPON_CREATE_MONEY_DISCOUNT_SUM)
    @PostMapping(path = POST_COUPON_CREATE_SUB_PATH)
    public ResponseEntity<ResponseAPI> createCoupon(@RequestBody @Valid CreateCouponRequest body, @PathVariable("couponType")CouponType couponType) {
        couponService.createMoneyCoupon(body, couponType);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.CREATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.CREATED);
    }

    @Operation(summary = COUPON_CREATE_BUY_GET_PRODUCT_GIFT_SUM)
    @PostMapping(path = POST_COUPON_CREATE_BUY_GET_TYPE_SUB_PATH)
    public ResponseEntity<ResponseAPI> createBuyGetCoupon(@RequestBody @Valid CreateBuyGetCouponRequest body) {
        couponService.createBuyGetCoupon(body);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.CREATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.CREATED);
    }

    @Operation(summary = COUPON_UPDATE_MONEY_BY_ID_SUM)
    @PutMapping(path = PUT_COUPON_UPDATE_MONEY_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateMoneyCouponById(@RequestBody @Valid UpdateMoneyCouponRequest body, @PathVariable(COUPON_ID) String couponId, @PathVariable("couponType") CouponType couponType) {
        couponService.updateMoneyCoupon(body, couponId, couponType);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.UPDATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }
    @Operation(summary = COUPON_UPDATE_PRODUCT_GIFT_BY_ID_SUM)
    @PutMapping(path = PUT_COUPON_UPDATE_PRODUCT_GIFT_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateProductGiftCouponById(@RequestBody @Valid UpdateProductGiftCouponRequest body, @PathVariable(COUPON_ID) String couponId, @PathVariable("couponType") CouponType couponType) {
        couponService.updateProductGiftCoupon(body, couponId, couponType);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.UPDATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = COUPON_DELETE_BY_ID_SUM)
    @DeleteMapping(path = DELETE_COUPON_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> deleteCouponById(@PathVariable(COUPON_ID) String couponId) {
        couponService.deleteCoupon(couponId);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.DELETED)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = COUPON_GET_RELEASE_LIST_SUM)
    @GetMapping(path = GET_COUPON_RELEASE_LIST_SUB_PATH)
    public ResponseEntity<ResponseAPI> getReleaseCouponList() {
        List<GetReleaseCouponListResponse> resData = couponService.getReleaseCouponList();

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = COUPON_GET_RELEASE_BY_ID_SUM)
    @GetMapping(path = GET_COUPON_RELEASE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getReleaseCouponById(@PathVariable(COUPON_ID) String couponId) {
        List<GetReleaseCouponByIdResponse> resData = couponService.getReleaseCouponById(couponId);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = COUPON_GET_LIST_SUM)
    @GetMapping(path = GET_COUPON_LIST_SUB_PATH)
    public ResponseEntity<ResponseAPI> getCouponList() {
        List<GetCouponListResponse> resData = couponService.getCouponList();

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }
    @Operation(summary = COUPON_GET_BY_ID_SUM)
    @GetMapping(path = GET_COUPON_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getCouponById(@PathVariable(COUPON_ID) String couponId) {
        GetCouponDetailsByIdResponse resData = couponService.getCouponById(couponId);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }
}
