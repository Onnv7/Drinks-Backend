package com.hcmute.drink.utils;

import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {


    public static String getCurrentUserId() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
    }

    public static void checkUserId(String userId) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal.getClass() != UserPrincipal.class) {
            throw new CustomException(ErrorConstant.PRINCIPAL_INVALID);
        }
        if(!((UserPrincipal) principal).getUserId().equals(userId)) {
            throw new CustomException(ErrorConstant.USER_ID_INVALID);
        }
    }
}
