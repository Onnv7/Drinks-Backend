package com.hcmute.drink.constant;

import static com.hcmute.drink.constant.RouterConstant.*;

public class SecurityConstant {
    public static final String SET_ADMIN_ROLE = "hasRole('ADMIN')";
    public static final String SET_USER_ROLE = "hasRole('USER')";





    // ALL =================================================================
    public static final String[] GET_AUTH_WHITELIST = {
            "/api/test/**",
            "/api/payment/**",
            "/create_payment",
            "/IPN/**",
            "/openapi/**", "/v3/api-docs/**", "/openapi/swagger-config/**",
            "/v3/api-docs.yaml", "/swagger-ui/**", "/swagger-ui.html",
            PRODUCT_GET_ALL_PATH, PRODUCT_GET_BY_ID_PATH,
            CATEGORY_GET_ALL_PATH, CATEGORY_GET_BY_ID_PATH,
            USER_CHECK_EXISTED_PATH,

    };
    public static final String[] POST_AUTH_WHITELIST = {
            "/refund",
            "/IPN/**",
            EMPLOYEE_LOGIN_PATH,
            AUTH_SEND_OPT_PATH, AUTH_SEND_CODE_TO_REGISTER_PATH, AUTH_RE_SEND_EMAIL_PATH,
                AUTH_REGISTER_PATH, AUTH_LOGIN_PATH, AUTH_VERIFY_EMAIL_PATH, AUTH_SEND_CODE_TO_GET_PWD_PATH
    };

    public static final String[] PATCH_AUTH_WHITELIST = {
            AUTH_CHANGE_PASSWORD_PATH
    };
    // Only USER =================================================================
    public static final String[] POST_USER_PATH = {
            ADDRESS_CREATE_PATH
    };
    public static final String[] PATCH_USER_PATH = {
            USER_CHANGE_PASSWORD_PATH
    };
    public static final String[] PUT_USER_PATH = {
            ADDRESS_UPDATE_PATH
    };
    public static final String[] DELETE_USER_PATH = {
            ADDRESS_DELETE_PATH
    };

    // Only ADMIN =================================================================
    public static final String[] ADMIN_PATH = {

    };
    public static final String[] GET_ADMIN_PATH = {
            USER_GET_ALL_PATH,
            EMPLOYEE_GET_ALL_PATH
    };
    public static final String[] PUT_ADMIN_PATH = {
            PRODUCT_UPDATE_BY_ID_PATH,
            CATEGORY_UPDATE_BY_ID_PATH
    };
    public static final String[] POST_ADMIN_PATH = {
            PRODUCT_CREATE_PATH,
            EMPLOYEE_REGISTER_PATH,
            CATEGORY_CREATE_PATH
    };

    public static final String[] DELETE_ADMIN_PATH = {
            PRODUCT_DELETE_BY_ID_PATH,
            EMPLOYEE_DELETE_BY_ID_PATH,
            CATEGORY_DELETE_BY_ID_PATH
    };

    // Only EMPLOYEE =================================================================
    public static final String[] EMPLOYEE_PATH = {};



    // ADMIN + EMPLOYEE =================================================================
    public static final String[] GET_ADMIN_EMPLOYEE_PATH = {
            EMPLOYEE_GET_BY_ID_PATH
    };

    public static final String[] PUT_ADMIN_EMPLOYEE_PATH = {
            EMPLOYEE_UPDATE_BY_ID_PATH,
    };
    public static final String[] PATCH_ADMIN_EMPLOYEE_PATH = {
            ORDER_UPDATE_STATUS_PATH,
            TRANSACTION_UPDATE_BY_ID_PATH
    };


    // ADMIN + USER =================================================================
    public static final String[] GET_ADMIN_USER_PATH = {
            USER_GET_BY_ID_PATH,
    };

    public static final String[] POST_ADMIN_USER_PATH = {
//            TRANSACTION_CREATE_PATH,
//            ORDER_CREATE_PATH,
    };

    public static final String[] PUT_ADMIN_USER_PATH = {
            USER_UPDATE_BY_ID_PATH,
    };



    // EMPLOYEE + USER =================================================================

    public static final String[] POST_EMPLOYEE_USER_PATH = {
            ORDER_CREATE_PATH,
    };
}
