package com.hcmute.drink.constant;

public class RouterConstant {
    // =================================================
    public static final String USER_BASE_PATH = "/api/user";
    public static final String ADDRESS_BASE_PATH = "/api/address";
    public static final String BRANCH_BASE_PATH = "/api/branch";
    public static final String AUTH_BASE_PATH = "/api/auth";
    public static final String CATEGORY_BASE_PATH = "/api/category";
    public static final String PRODUCT_BASE_PATH = "/api/product";
    public static final String EMPLOYEE_BASE_PATH = "/api/employee";
    public static final String ORDER_BASE_PATH = "/api/order";
    public static final String TRANSACTION_BASE_PATH = "/api/transaction";
    // ALL PATH=================================================
    public static final String USER_ALL_PATH = USER_BASE_PATH + "/**";
    public static final String AUTH_ALL_PATH = AUTH_BASE_PATH + "/**";
    public static final String CATEGORY_ALL_PATH = CATEGORY_BASE_PATH + "/**";
    public static final String PRODUCT_ALL_PATH = PRODUCT_BASE_PATH + "/**";
    public static final String ORDER_ALL_PATH = PRODUCT_BASE_PATH + "/**";
    // ID PATH =================================================================

    public static final String CATEGORY_ID_PATH = "/{categoryId}";
    public static final String CATEGORY_ID = "categoryId";
    public static final String PRODUCT_ID_PATH = "/{productId}";
    public static final String PRODUCT_ID = "productId";

    public static final String USER_ID_PATH = "/{userId}";
    public static final String USER_ID = "userId";
    public static final String ADDRESS_ID_PATH = "/{addressId}";
    public static final String ADDRESS_ID = "addressId";
    public static final String EMPLOYEE_ID_PATH = "/{employeeId}";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String ORDER_ID_PATH = "/{orderId}";
    public static final String ORDER_ID = "orderId";
    public static final String TRANSACTION_ID_PATH = "/{transId}";
    public static final String TRANSACTION_ID = "transId";
    public static final String BRANCH_ID_PATH = "/{branchId}";
    public static final String BRANCH_ID = "branchId";

    // ENDPOINT URL USER =================================================================
    public static final String USER_UPDATE_BY_ID_SUB_PATH = USER_ID_PATH;
    public static final String USER_UPDATE_BY_ID_PATH = USER_BASE_PATH + USER_UPDATE_BY_ID_SUB_PATH;
    public static final String USER_CHANGE_PASSWORD_SUB_PATH = USER_ID_PATH + "/change-password";
    public static final String USER_CHANGE_PASSWORD_PATH = USER_BASE_PATH + USER_CHANGE_PASSWORD_SUB_PATH;
    public static final String USER_GET_BY_ID_SUB_PATH = USER_ID_PATH + "/view";
    public static final String USER_GET_BY_ID_PATH = USER_BASE_PATH + USER_GET_BY_ID_SUB_PATH;
    public static final String USER_GET_ALL_SUB_PATH = "";
    public static final String USER_GET_ALL_PATH = USER_BASE_PATH + USER_GET_ALL_SUB_PATH;
    public static final String USER_CHECK_EXISTED_SUB_PATH = "/registered";
    public static final String USER_CHECK_EXISTED_PATH = USER_BASE_PATH + USER_CHECK_EXISTED_SUB_PATH;

    // ENDPOINT URL ADDRESS =================================================================
    public static final String POST_ADDRESS_CREATE_SUB_PATH = "/user" + USER_ID_PATH;
    public static final String POST_ADDRESS_CREATE_PATH = ADDRESS_BASE_PATH + POST_ADDRESS_CREATE_SUB_PATH;
    public static final String PUT_ADDRESS_UPDATE_SUB_PATH = ADDRESS_ID_PATH;
    public static final String PUT_ADDRESS_UPDATE_PATH = ADDRESS_BASE_PATH + PUT_ADDRESS_UPDATE_SUB_PATH;
    public static final String DELETE_ADDRESS_BY_ID_SUB_PATH = ADDRESS_ID_PATH;
    public static final String DELETE_ADDRESS_BY_ID_PATH = ADDRESS_BASE_PATH + DELETE_ADDRESS_BY_ID_SUB_PATH;
    public static final String GET_ADDRESS_BY_USER_ID_SUB_PATH = "/user" + USER_ID_PATH;
    public static final String GET_ADDRESS_BY_USER_ID_PATH = ADDRESS_BASE_PATH + GET_ADDRESS_BY_USER_ID_SUB_PATH;
    public static final String GET_ADDRESS_DETAILS_BY_ID_SUB_PATH = ADDRESS_ID_PATH;
    public static final String GET_ADDRESS_DETAILS_BY_ID_PATH = ADDRESS_BASE_PATH + GET_ADDRESS_DETAILS_BY_ID_SUB_PATH;

    // ENDPOINT URL PRODUCT =================================================================
    public static final String GET_PRODUCT_DETAILS_BY_ID_SUB_PATH = PRODUCT_ID_PATH + "/details";
    public static final String GET_PRODUCT_DETAILS_BY_ID_PATH = PRODUCT_BASE_PATH + GET_PRODUCT_DETAILS_BY_ID_SUB_PATH;
    public static final String GET_PRODUCT_ENABLED_BY_ID_SUB_PATH = PRODUCT_ID_PATH + "/view";
    public static final String GET_PRODUCT_ENABLED_BY_ID_PATH = PRODUCT_BASE_PATH + GET_PRODUCT_ENABLED_BY_ID_SUB_PATH;
    public static final String GET_PRODUCT_BY_CATEGORY_ID_SUB_PATH = "/category" + CATEGORY_ID_PATH;
    public static final String GET_PRODUCT_BY_CATEGORY_ID_PATH = PRODUCT_BASE_PATH + GET_PRODUCT_BY_CATEGORY_ID_SUB_PATH;
    public static final String GET_PRODUCT_ALL_ENABLED_SUB_PATH = "/visible";
    public static final String GET_PRODUCT_ALL_ENABLED_PATH = PRODUCT_BASE_PATH + GET_PRODUCT_ALL_ENABLED_SUB_PATH;
    public static final String GET_PRODUCT_ALL_SUB_PATH = "";
    public static final String GET_PRODUCT_ALL_PATH = PRODUCT_BASE_PATH + GET_PRODUCT_ALL_SUB_PATH;
    public static final String PUT_PRODUCT_UPDATE_BY_ID_SUB_PATH = PRODUCT_ID_PATH;
    public static final String PUT_PRODUCT_UPDATE_BY_ID_PATH = PRODUCT_BASE_PATH + PUT_PRODUCT_UPDATE_BY_ID_SUB_PATH;
    public static final String DELETE_PRODUCT_BY_ID_SUB_PATH = PRODUCT_ID_PATH;
    public static final String DELETE_PRODUCT_BY_ID_PATH = PRODUCT_BASE_PATH + DELETE_PRODUCT_BY_ID_SUB_PATH;
    public static final String POST_PRODUCT_CREATE_SUB_PATH = "";
    public static final String POST_PRODUCT_CREATE_PATH = PRODUCT_BASE_PATH + POST_PRODUCT_CREATE_SUB_PATH;
    public static final String GET_PRODUCT_TOP_QUANTITY_ORDER_SUB_PATH = "/top/{itemQuantity}";
    public static final String GET_PRODUCT_TOP_QUANTITY_ORDER_PATH = PRODUCT_BASE_PATH + GET_PRODUCT_TOP_QUANTITY_ORDER_SUB_PATH;


    // ENDPOINT URL EMPLOYEE =================================================================

    public static final String GET_EMPLOYEE_BY_ID_SUB_PATH = EMPLOYEE_ID_PATH;
    public static final String GET_EMPLOYEE_BY_ID_PATH = EMPLOYEE_BASE_PATH + GET_EMPLOYEE_BY_ID_SUB_PATH;
    public static final String GET_EMPLOYEE_ALL_SUB_PATH = "";
    public static final String GET_EMPLOYEE_ALL_PATH = EMPLOYEE_BASE_PATH + GET_EMPLOYEE_ALL_SUB_PATH;
    public static final String PUT_EMPLOYEE_UPDATE_BY_ID_SUB_PATH = EMPLOYEE_ID_PATH;
    public static final String PUT_EMPLOYEE_UPDATE_BY_ID_PATH = EMPLOYEE_BASE_PATH + PUT_EMPLOYEE_UPDATE_BY_ID_SUB_PATH;
    public static final String PATCH_EMPLOYEE_UPDATE_PASSWORD_BY_ID_SUB_PATH = EMPLOYEE_ID_PATH + "/update-password";
    public static final String PATCH_EMPLOYEE_UPDATE_PASSWORD_BY_ID_PATH = EMPLOYEE_BASE_PATH + PATCH_EMPLOYEE_UPDATE_PASSWORD_BY_ID_SUB_PATH;
    public static final String DELETE_EMPLOYEE_BY_ID_SUB_PATH = EMPLOYEE_ID_PATH;
    public static final String DELETE_EMPLOYEE_BY_ID_PATH = EMPLOYEE_BASE_PATH + DELETE_EMPLOYEE_BY_ID_SUB_PATH;
    public static final String POST_EMPLOYEE_REGISTER_SUB_PATH = "";
    public static final String POST_EMPLOYEE_REGISTER_PATH = EMPLOYEE_BASE_PATH + POST_EMPLOYEE_REGISTER_SUB_PATH;
    public static final String PATCH_EMPLOYEE_UPDATE_PASSWORD_SUB_PATH = EMPLOYEE_ID_PATH + "/change-password";
    public static final String PATCH_EMPLOYEE_UPDATE_PASSWORD_PATH = EMPLOYEE_BASE_PATH + PATCH_EMPLOYEE_UPDATE_PASSWORD_SUB_PATH;

    // ENDPOINT URL CATEGORY =================================================================

    public static final String GET_CATEGORY_BY_SUB_ID_PATH =  CATEGORY_ID_PATH + "/details";
    public static final String GET_CATEGORY_BY_ID_PATH = CATEGORY_BASE_PATH + GET_CATEGORY_BY_SUB_ID_PATH;
    public static final String GET_CATEGORY_ALL_SUB_PATH = "";
    public static final String GET_CATEGORY_ALL_PATH = CATEGORY_BASE_PATH + GET_CATEGORY_ALL_SUB_PATH;
    public static final String GET_CATEGORY_ALL_WITHOUT_DELETED_SUB_PATH = "/visible";
    public static final String GET_CATEGORY_ALL_WITHOUT_DELETED_PATH = CATEGORY_BASE_PATH + GET_CATEGORY_ALL_WITHOUT_DELETED_SUB_PATH;
    public static final String PUT_CATEGORY_UPDATE_BY_ID_SUB_PATH =  CATEGORY_ID_PATH;
    public static final String PUT_CATEGORY_UPDATE_BY_ID_PATH = CATEGORY_BASE_PATH + PUT_CATEGORY_UPDATE_BY_ID_SUB_PATH;
    public static final String DELETE_CATEGORY_BY_ID_SUB_PATH =  CATEGORY_ID_PATH;
    public static final String DELETE_CATEGORY_BY_ID_PATH = CATEGORY_BASE_PATH + DELETE_CATEGORY_BY_ID_SUB_PATH;
    public static final String POST_CATEGORY_CREATE_SUB_PATH = "";
    public static final String POST_CATEGORY_CREATE_PATH =  CATEGORY_BASE_PATH + POST_CATEGORY_CREATE_SUB_PATH;

    // ENDPOINT URL ORDER =================================================================
    public static final String POST_ORDER_CREATE_SUB_PATH = "";
    public static final String POST_ORDER_CREATE_PATH = ORDER_BASE_PATH + POST_ORDER_CREATE_SUB_PATH;
    public static final String PATCH_ORDER_UPDATE_STATUS_SUB_PATH = ORDER_ID_PATH + "/by/{maker}";
    public static final String PATCH_ORDER_UPDATE_STATUS_PATH = ORDER_BASE_PATH + PATCH_ORDER_UPDATE_STATUS_SUB_PATH;
    public static final String GET_ORDER_ALL_SHIPPING_SUB_PATH = "/shipping";
    public static final String GET_ORDER_ALL_SHIPPING_PATH = ORDER_BASE_PATH + GET_ORDER_ALL_SHIPPING_SUB_PATH;
    public static final String GET_ORDER_ALL_BY_STATUS_AND_TYPE_SUB_PATH = "/{orderType}/status/{orderStatus}";
    public static final String GET_ORDER_ALL_BY_STATUS_AND_TYPE_PATH = ORDER_BASE_PATH + GET_ORDER_ALL_BY_STATUS_AND_TYPE_SUB_PATH;
    public static final String GET_ORDER_DETAILS_BY_ID_SUB_PATH = ORDER_ID_PATH + "/details";
    public static final String GET_ORDER_DETAILS_BY_ID_PATH = ORDER_BASE_PATH + GET_ORDER_DETAILS_BY_ID_SUB_PATH;
    public static final String GET_ORDER_ORDERS_BY_USER_ID_AND_ORDER_STATUS_SUB_PATH = "/history/user" + USER_ID_PATH;
    public static final String GET_ORDER_ORDERS_BY_USER_ID_AND_ORDER_STATUS_PATH = ORDER_BASE_PATH + GET_ORDER_ORDERS_BY_USER_ID_AND_ORDER_STATUS_SUB_PATH;
    public static final String POST_ORDER_CREATE_REVIEW_SUB_PATH = "/rating" + ORDER_ID_PATH;
    public static final String POST_ORDER_CREATE_REVIEW_PATH = ORDER_BASE_PATH + POST_ORDER_CREATE_REVIEW_SUB_PATH;
    public static final String GET_ORDER_STATUS_LINE_SUB_PATH = ORDER_ID_PATH + "/status-line" ;
    public static final String GET_ORDER_STATUS_LINE_PATH = ORDER_BASE_PATH + GET_ORDER_STATUS_LINE_SUB_PATH;
    public static final String GET_ORDER_ALL_ORDER_HISTORY_FOR_EMPLOYEE_SUB_PATH = "/history/{orderStatus}";
    public static final String GET_ORDER_ALL_ORDER_HISTORY_FOR_EMPLOYEE_PATH = ORDER_BASE_PATH + GET_ORDER_ALL_ORDER_HISTORY_FOR_EMPLOYEE_SUB_PATH;
    public static final String GET_ORDER_ORDER_QUANTITY_BY_STATUS_SUB_PATH = "/quantity/today";
    public static final String GET_ORDER_ORDER_QUANTITY_BY_STATUS_PATH = ORDER_BASE_PATH + GET_ORDER_ORDER_QUANTITY_BY_STATUS_SUB_PATH;

    // ENDPOINT URL TRANSACTION =================================================================
    public static final String PATCH_TRANSACTION_UPDATE_BY_ID_SUB_PATH =  TRANSACTION_ID_PATH;
    public static final String PATCH_TRANSACTION_UPDATE_BY_ID_PATH = TRANSACTION_BASE_PATH + PATCH_TRANSACTION_UPDATE_BY_ID_SUB_PATH;
    public static final String PATCH_TRANSACTION_UPDATE_COMPLETE_SUB_PATH = TRANSACTION_ID_PATH + "/complete";
    public static final String PATCH_TRANSACTION_UPDATE_COMPLETE_PATH = TRANSACTION_BASE_PATH + PATCH_TRANSACTION_UPDATE_COMPLETE_SUB_PATH;

    public static final String GET_TRANSACTION_REVENUE_BY_TIME_SUB_PATH = "/revenue";
    public static final String GET_TRANSACTION_REVENUE_BY_TIME_PATH = TRANSACTION_BASE_PATH + GET_TRANSACTION_REVENUE_BY_TIME_SUB_PATH;
    public static final String GET_TRANSACTION_REVENUE_CURRENT_DATE_SUB_PATH = "/revenue/today";
    public static final String GET_TRANSACTION_REVENUE_CURRENT_DATE_PATH = TRANSACTION_BASE_PATH + GET_TRANSACTION_REVENUE_CURRENT_DATE_SUB_PATH;


    // ENDPOINT URL AUTH =================================================================
    public static final String POST_AUTH_SEND_OPT_SUB_PATH = "/send-opt";
    public static final String POST_AUTH_SEND_OPT_PATH = AUTH_BASE_PATH + POST_AUTH_SEND_OPT_SUB_PATH;
    public static final String POST_AUTH_SEND_CODE_TO_REGISTER_SUB_PATH = "/register/send-code";
    public static final String POST_AUTH_SEND_CODE_TO_REGISTER_PATH = AUTH_BASE_PATH + POST_AUTH_SEND_CODE_TO_REGISTER_SUB_PATH;
    public static final String POST_AUTH_SEND_CODE_TO_GET_PWD_SUB_PATH = "/password/send-code";
    public static final String POST_AUTH_SEND_CODE_TO_GET_PWD_PATH = AUTH_BASE_PATH + POST_AUTH_SEND_CODE_TO_GET_PWD_SUB_PATH;
    public static final String POST_AUTH_RE_SEND_EMAIL_SUB_PATH = "/resend-email";
    public static final String POST_AUTH_RE_SEND_EMAIL_PATH = AUTH_BASE_PATH + POST_AUTH_RE_SEND_EMAIL_SUB_PATH;
    public static final String POST_AUTH_REGISTER_SUB_PATH = "/user/register";
    public static final String POST_AUTH_REGISTER_PATH = AUTH_BASE_PATH + POST_AUTH_REGISTER_SUB_PATH;
    public static final String POST_AUTH_LOGIN_SUB_PATH = "/user/login";
    public static final String POST_AUTH_LOGIN_PATH = AUTH_BASE_PATH + POST_AUTH_LOGIN_SUB_PATH;
    public static final String POST_AUTH_VERIFY_EMAIL_SUB_PATH = "/verify";
    public static final String POST_AUTH_VERIFY_EMAIL_PATH = AUTH_BASE_PATH + POST_AUTH_VERIFY_EMAIL_SUB_PATH;
    public static final String PATCH_AUTH_CHANGE_PASSWORD_SUB_PATH = "/change-password";
    public static final String PATCH_AUTH_CHANGE_PASSWORD_PATH = AUTH_BASE_PATH + PATCH_AUTH_CHANGE_PASSWORD_SUB_PATH;
    public static final String POST_AUTH_EMPLOYEE_LOGIN_SUB_PATH = "/employee/login";
    public static final String POST_AUTH_EMPLOYEE_LOGIN_PATH = AUTH_BASE_PATH + POST_AUTH_EMPLOYEE_LOGIN_SUB_PATH;
    public static final String POST_AUTH_REFRESH_TOKEN_SUB_PATH = "/user/refresh-token";
    public static final String POST_AUTH_REFRESH_TOKEN_PATH = AUTH_BASE_PATH + POST_AUTH_REFRESH_TOKEN_SUB_PATH;
    public static final String POST_AUTH_REFRESH_EMPLOYEE_TOKEN_SUB_PATH = "/employee/refresh-token";
    public static final String POST_AUTH_REFRESH_EMPLOYEE_TOKEN_PATH = AUTH_BASE_PATH + POST_AUTH_REFRESH_EMPLOYEE_TOKEN_SUB_PATH;
    // ENDPOINT URL BRANCH =================================================================
    public static final String POST_BRANCH_CREATE_SUB_PATH = "";
    public static final String POST_BRANCH_CREATE_PATH = BRANCH_BASE_PATH + POST_BRANCH_CREATE_SUB_PATH;
    public static final String PUT_BRANCH_UPDATE_SUB_PATH = BRANCH_ID_PATH;
    public static final String PUT_BRANCH_UPDATE_PATH = BRANCH_BASE_PATH + PUT_BRANCH_UPDATE_SUB_PATH;

}
