package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.UserCouponCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;

    public void saveUserCoupon(UserCouponCollection userCoupon) {
        userCouponRepository.save(userCoupon);
    }

    public void checkCouponIsUsed(List<String> couponCodeList, String userId) {
        for (String couponCode : couponCodeList) {
            UserCouponCollection userCoupon = userCouponRepository.findByUserIdAndCouponCode(new ObjectId(userId), couponCode).orElse(null);
            if(userCoupon != null) {
                throw new CustomException(ErrorConstant.COUPON_IS_USED);
            }
        }
    }

    public UserCouponCollection findByUserIdAndCouponCode(String userId, String couponCode) {
        return userCouponRepository.findByUserIdAndCouponCode(new ObjectId(userId), couponCode).orElse(null);
    }
}
