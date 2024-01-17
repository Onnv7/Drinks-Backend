package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.CouponCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.collection.embedded.*;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateBuyGetCouponRequest;
import com.hcmute.drink.dto.request.CreateCouponRequest;
import com.hcmute.drink.dto.request.UpdateCouponRequest;
import com.hcmute.drink.dto.response.GetReleaseCouponByIdResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponListResponse;
import com.hcmute.drink.enums.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.CouponRepository;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.service.database.ICouponService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService {
    private final CouponRepository couponRepository;
    private final UserCouponService userCouponService;
    private final ModelMapperService modelMapperService;
    private final ProductService productService;

    private void setConditionList(List<CouponConditionEmbedded> conditionList) {
        for (CouponConditionEmbedded condition : conditionList) {
            Class<?> className = condition.getValue().getClass();
            if (className.equals(String.class) && (condition.getType() == ConditionType.SPECIFIC_PRODUCT || condition.getType() == ConditionType.SPECIFIC_CATEGORY)) {
                condition.setValue(new ObjectId(condition.getValue().toString()));

            } else if (condition.getValue() instanceof Number && condition.getType() == ConditionType.TOTAL_BILL) {
                condition.setValue(((Number) condition.getValue()).longValue());
            } else if (className.equals(ArrayList.class) && condition.getType() == ConditionType.PRODUCT_LIST && !((ArrayList<?>) condition.getValue()).isEmpty()) {
                List<ObjectId> productIds = new ArrayList<>();
                for (String id : (ArrayList<String>) condition.getValue()) {
                    productIds.add(new ObjectId(id));
                }
                condition.setValue(productIds);
            } else {
                throw new CustomException(ErrorConstant.CONDITION_VALUE_INVALID);
            }
        }
    }




    public CouponCollection getById(String id) {
        return couponRepository.getById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    public CouponCollection getByCode(String couponCode) {
        return couponRepository.getByCode(couponCode).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + couponCode));
    }
    public CouponCollection getAndCheckValidCoupon(String couponCode) {
        CouponCollection coupon = couponRepository.getByCode(couponCode).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + couponCode));
        if(coupon.getStatus() != CouponStatus.RELEASED) {
            throw new CustomException(ErrorConstant.COUPON_UNRELEASED);
        } else if(coupon.getQuantity() != null && coupon.getQuantity() > 0) {
            throw new CustomException(ErrorConstant.COUPON_OUT_OF_QUANTITY);
        }
        return coupon;
    }


    public MoneyDiscountEmbedded checkMoneyCouponOrderBill(CouponCollection coupon, Long total) {
        List<CouponConditionEmbedded> conditionList = coupon.getConditionList();
        for (CouponConditionEmbedded condition : conditionList) {
            if (condition.getType() == ConditionType.TOTAL_BILL) {
                if (total < ((Number) condition.getValue()).longValue()) {
                    throw new CustomException("This code invalid");
                }
            } else {
                throw new CustomException("This code invalid");
            }
        }
        if (coupon.getQuantity() != null) {
            coupon.setQuantity(coupon.getQuantity() - 1);
            couponRepository.save(coupon);
        }
        return coupon.getMoneyDiscount();
    }


    @Transactional
    public MoneyDiscountEmbedded checkMoneyCouponProduct(CouponCollection coupon, String productId) {
        List<CouponConditionEmbedded> conditionList = coupon.getConditionList();
        checkConditionForProductCoupon(conditionList, productId);
        if (coupon.getQuantity() != null) {
            coupon.setQuantity(coupon.getQuantity() - 1);
            couponRepository.save(coupon);
        }
        return coupon.getMoneyDiscount();
    }

    @Transactional
    public ProductGiftEmbedded checkBuyGetCouponProduct(CouponCollection coupon, String productId) {
        List<CouponConditionEmbedded> conditionList = coupon.getConditionList();
        checkConditionForProductCoupon(conditionList, productId);
        if (coupon.getQuantity() != null) {
            coupon.setQuantity(coupon.getQuantity() - 1);
            couponRepository.save(coupon);
        }
        return coupon.getProductGift();
    }

    public void checkConditionForProductCoupon( List<CouponConditionEmbedded> conditionList, String productId) {

        for (CouponConditionEmbedded condition : conditionList) {
            if (condition.getType() == ConditionType.SPECIFIC_PRODUCT) {
                String conditionValue = ((ObjectId) condition.getValue()).toString();
                if (!productId.equals(conditionValue)) {
                    throw new CustomException(ErrorConstant.COUPON_INVALID);
                }
            } else if (condition.getType() == ConditionType.PRODUCT_LIST) {
                ObjectId idValue = new ObjectId(productId);
                List<ObjectId> conditionValue = (List<ObjectId>) condition.getValue();
                if (!conditionValue.contains(idValue)) {
                    throw new CustomException(ErrorConstant.COUPON_INVALID);
                }
            } else if (condition.getType() == ConditionType.SPECIFIC_CATEGORY) {
                String conditionValue = ((ObjectId) condition.getValue()).toString();
                if (!productId.equals(conditionValue)) {
                    throw new CustomException(ErrorConstant.COUPON_INVALID);
                }
            }
            else {
                throw new CustomException(ErrorConstant.COUPON_INVALID);
            }
        }
    }

    @Override
    public void createMoneyCoupon(CreateCouponRequest body, CouponType couponType) {
        if(couponType  == CouponType.BUY_PRODUCT_GET_PRODUCT ) {
            throw new CustomException(ErrorConstant.COUPON_TYPE_INVALID);
        }
        CouponCollection coupon = modelMapperService.mapClass(body, CouponCollection.class);
        setConditionList(coupon.getConditionList());
        coupon.setStatus(CouponStatus.UNRELEASED);
        coupon.setCouponType(couponType);
        couponRepository.save(coupon);
    }

    @Override
    public void createBuyGetCoupon(CreateBuyGetCouponRequest body) {
        CouponCollection coupon = modelMapperService.mapClass(body, CouponCollection.class);
        setConditionList(coupon.getConditionList());
        ProductCollection product = productService.getById(body.getProductGift().getProductId().toString());
        long count = product.getSizeList().stream().filter(it -> it.getSize().equals(body.getProductGift().getSize())).count();
        if(count != 1) {
            throw  new CustomException(ErrorConstant.SIZE_PRODUCT_INVALID);
        }
        coupon.getProductGift().setProductName(product.getName());
        coupon.setStatus(CouponStatus.UNRELEASED);
        coupon.setCouponType(CouponType.BUY_PRODUCT_GET_PRODUCT);
        couponRepository.save(coupon);
    }

    @Override
    public void updateCoupon(UpdateCouponRequest body, String couponId) {
        CouponCollection couponCollection = getById(couponId);
        if(couponCollection.getStatus() == CouponStatus.UNRELEASED) {
            throw new CustomException(ErrorConstant.COUPON_STATUS_UNRELEASED);
        }
        modelMapperService.map(body, couponCollection);
        setConditionList(couponCollection.getConditionList());
        couponRepository.save(couponCollection);
    }

    @Override
    public void deleteCoupon(String couponId) {
        CouponCollection couponCollection = getById(couponId);
        if(couponCollection.getStatus() == CouponStatus.UNRELEASED) {
            throw new CustomException(ErrorConstant.COUPON_STATUS_UNRELEASED);
        }
        couponCollection.setDeleted(true);
        couponRepository.save(couponCollection);
    }

    @Override
    public List<GetReleaseCouponListResponse> getReleaseCouponList() {
        return couponRepository.getReleaseCouponList();
    }

    @Override
    public List<GetReleaseCouponByIdResponse> getReleaseCouponById(String couponId) {
        return couponRepository.getReleaseCouponById(couponId);
    }


}
