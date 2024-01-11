package com.hcmute.drink.constant;

import static com.hcmute.drink.constant.RouterConstant.*;

public class SecurityConstant {
    public static final String SET_ADMIN_ROLE = "hasRole('ADMIN')";
    public static final String SET_USER_ROLE = "hasRole('USER')";


    // ALL =================================================================
    public static final String[] GET_AUTH_WHITELIST = {
            "/api/payment/**",
            "/IPN/**",
            "/openapi/**", "/v3/api-docs/**", "/openapi/swagger-config/**",
            "/v3/api-docs.yaml", "/swagger-ui/**", "/swagger-ui.html",
            GET_PRODUCT_ALL_ENABLED_PATH, GET_PRODUCT_BY_CATEGORY_ID_PATH, GET_PRODUCT_ENABLED_BY_ID_PATH, GET_PRODUCT_TOP_QUANTITY_ORDER_PATH,
            GET_CATEGORY_BY_ID_PATH,
            USER_CHECK_EXISTED_PATH, GET_CATEGORY_ALL_WITHOUT_DELETED_PATH, "/tool/**"


    };
    public static final String[] POST_AUTH_WHITELIST = {
            "/refund",
            "/IPN/**",
            POST_AUTH_EMPLOYEE_LOGIN_PATH, POST_AUTH_REFRESH_TOKEN_PATH, POST_AUTH_REFRESH_EMPLOYEE_TOKEN_PATH,
            POST_AUTH_SEND_OPT_PATH, POST_AUTH_SEND_CODE_TO_REGISTER_PATH, POST_AUTH_RE_SEND_EMAIL_PATH,
            POST_AUTH_REGISTER_PATH, POST_AUTH_LOGIN_PATH, POST_AUTH_VERIFY_EMAIL_PATH, POST_AUTH_SEND_CODE_TO_GET_PWD_PATH
    };

    public static final String[] PATCH_AUTH_WHITELIST = {
            PATCH_AUTH_CHANGE_PASSWORD_PATH
    };
    // Only USER =================================================================
    public static final String[] POST_USER_PATH = {
            POST_ADDRESS_CREATE_PATH,
            POST_ORDER_CREATE_REVIEW_PATH,
            USER_GET_BY_ID_PATH,
    };
    public static final String[] PATCH_USER_PATH = {
            USER_CHANGE_PASSWORD_PATH
    };
    public static final String[] PUT_USER_PATH = {
            PUT_ADDRESS_UPDATE_PATH,
            USER_UPDATE_BY_ID_PATH,
    };
    public static final String[] DELETE_USER_PATH = {
            DELETE_ADDRESS_BY_ID_PATH
    };
    public static final String[] GET_USER_PATH = {
            GET_ORDER_ORDERS_BY_USER_ID_AND_ORDER_STATUS_PATH,
            GET_ADDRESS_BY_USER_ID_PATH, GET_ADDRESS_DETAILS_BY_ID_PATH,
    };

    // Only ADMIN =================================================================
    public static final String[] ADMIN_PATH = {

    };
    public static final String[] GET_ADMIN_PATH = {
            GET_PRODUCT_DETAILS_BY_ID_PATH,
            USER_GET_ALL_PATH, GET_EMPLOYEE_ALL_PATH,
            GET_CATEGORY_ALL_PATH, GET_PRODUCT_ALL_PATH,
            GET_TRANSACTION_REVENUE_BY_TIME_PATH, GET_TRANSACTION_REVENUE_CURRENT_DATE_PATH,

    };
    public static final String[] PUT_ADMIN_PATH = {
            PUT_PRODUCT_UPDATE_BY_ID_PATH,
            PUT_CATEGORY_UPDATE_BY_ID_PATH, PUT_BRANCH_UPDATE_PATH
    };
    public static final String[] POST_ADMIN_PATH = {
            POST_PRODUCT_CREATE_PATH,
            POST_EMPLOYEE_REGISTER_PATH,
            POST_CATEGORY_CREATE_PATH,
            POST_BRANCH_CREATE_PATH,
    };
    public static final String[] PATCH_ADMIN_PATH = {
    };

    public static final String[] DELETE_ADMIN_PATH = {
            DELETE_PRODUCT_BY_ID_PATH,
            DELETE_EMPLOYEE_BY_ID_PATH,
            DELETE_CATEGORY_BY_ID_PATH,
    };

    // Only EMPLOYEE =================================================================
    public static final String[] GET_EMPLOYEE_PATH = {
    };

    public static final String[] PATCH_EMPLOYEE_PATH = {
            PATCH_TRANSACTION_UPDATE_COMPLETE_PATH,
            PATCH_EMPLOYEE_UPDATE_PASSWORD_PATH
    };

    // ADMIN + EMPLOYEE =================================================================
    public static final String[] GET_ADMIN_EMPLOYEE_PATH = {
            GET_EMPLOYEE_BY_ID_PATH,
            GET_ORDER_ALL_SHIPPING_PATH,
            GET_ORDER_ALL_BY_STATUS_AND_TYPE_PATH,
            GET_ORDER_ALL_ORDER_HISTORY_FOR_EMPLOYEE_PATH, GET_ORDER_ORDER_QUANTITY_BY_STATUS_PATH
    };

    public static final String[] PUT_ADMIN_EMPLOYEE_PATH = {
            PUT_EMPLOYEE_UPDATE_BY_ID_PATH,
    };
    public static final String[] PATCH_ADMIN_EMPLOYEE_PATH = {
            PATCH_EMPLOYEE_UPDATE_PASSWORD_BY_ID_PATH
    };


    // ADMIN + USER =================================================================
    public static final String[] GET_ADMIN_USER_PATH = {

    };
    public static final String[] PATCH_ADMIN_USER_PATH = {
            PATCH_TRANSACTION_UPDATE_BY_ID_PATH,

    };

    public static final String[] POST_ADMIN_USER_PATH = {
//            TRANSACTION_CREATE_PATH,
//            ORDER_CREATE_PATH,
    };

    public static final String[] PUT_ADMIN_USER_PATH = {
    };


    // EMPLOYEE + USER =================================================================
    public static final String[] GET_EMPLOYEE_USER_PATH = {
            GET_ORDER_STATUS_LINE_PATH
    };

    public static final String[] POST_EMPLOYEE_USER_PATH = {
            POST_ORDER_CREATE_PATH,
    };
    public static final String[] PATCH_EMPLOYEE_USER_PATH = {
            PATCH_ORDER_UPDATE_STATUS_PATH,
    };
    // ADMIN + EMPLOYEE + USER =================================================================

    public static final String[] GET_ADMIN_EMPLOYEE_USER_PATH = {
            GET_ORDER_DETAILS_BY_ID_PATH,
    };

}
