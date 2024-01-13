package com.hcmute.drink.utils;

import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class SecurityUtils {
    public void checkMySelf(String userId) {
        String myId = getCurrentUserId();
        if(!myId.equals(userId)) {
            throw new CustomException(ErrorConstant.CANT_ACCESS);
        }
    }

    public void exceptionIfNotMeAndNotAdmin(String userId) throws Exception {
        String myId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String role = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);
        // người không phải admin và khác id

        if(!myId.equals(userId) && !role.equals("ROLE_ADMIN")) {
            throw new Exception(ErrorConstant.CANT_ACCESS);
        }
    }

    public static String getCurrentUserId() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
    }
}
