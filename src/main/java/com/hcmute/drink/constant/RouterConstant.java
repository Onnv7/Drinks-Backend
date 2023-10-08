package com.hcmute.drink.constant;

public class RouterConstant {
    // =================================================
    public static final String USER_BASE_PATH = "/api/user";
    public static final String ADDRESS_BASE_PATH = "/api/address";
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

    // ENDPOINT URL USER =================================================================

    private static final String USER_ID = "{userId}";

    public static final String USER_UPDATE_BY_ID_SUB_PATH = "/update/" + USER_ID;
    public static final String USER_UPDATE_BY_ID_PATH = USER_BASE_PATH + USER_UPDATE_BY_ID_SUB_PATH;
    public static final String USER_CHANGE_PASSWORD_SUB_PATH = "/change-password/" + USER_ID;
    public static final String USER_CHANGE_PASSWORD_PATH = USER_BASE_PATH + USER_CHANGE_PASSWORD_SUB_PATH;
    public static final String USER_GET_BY_ID_SUB_PATH = "/" + USER_ID;
    public static final String USER_GET_BY_ID_PATH = USER_BASE_PATH + USER_GET_BY_ID_SUB_PATH;
    public static final String USER_GET_ALL_SUB_PATH = "";
    public static final String USER_GET_ALL_PATH = USER_BASE_PATH + USER_GET_ALL_SUB_PATH;
    public static final String USER_CHECK_EXISTED_SUB_PATH = "/existed";
    public static final String USER_CHECK_EXISTED_PATH = USER_BASE_PATH + USER_CHECK_EXISTED_SUB_PATH;

    // ENDPOINT URL ADDRESS =================================================================
    public static final String ADDRESS_ID = "{addressId}";
    public static final String ADDRESS_CREATE_SUB_PATH = "/" + USER_ID ;
    public static final String ADDRESS_CREATE_PATH = ADDRESS_BASE_PATH + ADDRESS_CREATE_SUB_PATH;
    public static final String ADDRESS_UPDATE_SUB_PATH = "/" + ADDRESS_ID ;
    public static final String ADDRESS_UPDATE_PATH = ADDRESS_BASE_PATH + ADDRESS_UPDATE_SUB_PATH;
    public static final String ADDRESS_DELETE_SUB_PATH = "/" + ADDRESS_ID ;
    public static final String ADDRESS_DELETE_PATH = ADDRESS_BASE_PATH + ADDRESS_DELETE_SUB_PATH;

    // ENDPOINT URL PRODUCT =================================================================
    public static final String PRODUCT_ID = "{productId}";

    public static final String PRODUCT_GET_BY_ID_SUB_PATH = "/" + PRODUCT_ID;
    public static final String PRODUCT_GET_BY_ID_PATH = PRODUCT_BASE_PATH + PRODUCT_GET_BY_ID_SUB_PATH;
    public static final String PRODUCT_GET_ALL_SUB_PATH = "";
    public static final String PRODUCT_GET_ALL_PATH = PRODUCT_BASE_PATH + PRODUCT_GET_ALL_SUB_PATH;
    public static final String PRODUCT_UPDATE_BY_ID_SUB_PATH = "/" + PRODUCT_ID;
    public static final String PRODUCT_UPDATE_BY_ID_PATH = PRODUCT_BASE_PATH + PRODUCT_UPDATE_BY_ID_SUB_PATH;
    public static final String PRODUCT_DELETE_BY_ID_SUB_PATH = "/" + PRODUCT_ID;
    public static final String PRODUCT_DELETE_BY_ID_PATH = PRODUCT_BASE_PATH + PRODUCT_DELETE_BY_ID_SUB_PATH;
    public static final String PRODUCT_CREATE_SUB_PATH = "";
    public static final String PRODUCT_CREATE_PATH = PRODUCT_BASE_PATH + PRODUCT_CREATE_SUB_PATH;


    // ENDPOINT URL EMPLOYEE =================================================================
    public static final String EMPLOYEE_ID = "{employeeId}";

    public static final String EMPLOYEE_GET_BY_ID_SUB_PATH = "/" + EMPLOYEE_ID;
    public static final String EMPLOYEE_GET_BY_ID_PATH = EMPLOYEE_BASE_PATH + EMPLOYEE_GET_BY_ID_SUB_PATH;
    public static final String EMPLOYEE_GET_ALL_SUB_PATH = "";
    public static final String EMPLOYEE_GET_ALL_PATH = EMPLOYEE_BASE_PATH + EMPLOYEE_GET_ALL_SUB_PATH;
    public static final String EMPLOYEE_UPDATE_BY_ID_SUB_PATH = "/" + EMPLOYEE_ID;
    public static final String EMPLOYEE_UPDATE_BY_ID_PATH = EMPLOYEE_BASE_PATH + EMPLOYEE_UPDATE_BY_ID_SUB_PATH;
    public static final String EMPLOYEE_DELETE_BY_ID_SUB_PATH = "/" + EMPLOYEE_ID;
    public static final String EMPLOYEE_DELETE_BY_ID_PATH = EMPLOYEE_BASE_PATH + EMPLOYEE_DELETE_BY_ID_SUB_PATH;
    public static final String EMPLOYEE_REGISTER_SUB_PATH = "/register";
    public static final String EMPLOYEE_REGISTER_PATH = EMPLOYEE_BASE_PATH + EMPLOYEE_REGISTER_SUB_PATH;

    // ENDPOINT URL CATEGORY =================================================================
    public static final String CATEGORY_ID = "{categoryId}";

    public static final String CATEGORY_GET_BY_SUB_ID_PATH = "/" + CATEGORY_ID;
    public static final String CATEGORY_GET_BY_ID_PATH = CATEGORY_BASE_PATH + CATEGORY_GET_BY_SUB_ID_PATH;
    public static final String CATEGORY_GET_ALL_SUB_PATH = "";
    public static final String CATEGORY_GET_ALL_PATH = CATEGORY_BASE_PATH + CATEGORY_GET_ALL_SUB_PATH;
    public static final String CATEGORY_UPDATE_BY_ID_SUB_PATH = "/update/" + CATEGORY_ID;
    public static final String CATEGORY_UPDATE_BY_ID_PATH = CATEGORY_BASE_PATH + CATEGORY_UPDATE_BY_ID_SUB_PATH;
    public static final String CATEGORY_DELETE_BY_ID_SUB_PATH = "/" + CATEGORY_ID;
    public static final String CATEGORY_DELETE_BY_ID_PATH = CATEGORY_BASE_PATH + CATEGORY_DELETE_BY_ID_SUB_PATH;
    public static final String CATEGORY_CREATE_SUB_PATH = "";
    public static final String CATEGORY_CREATE_PATH = "" + CATEGORY_CREATE_SUB_PATH;

    // ENDPOINT URL ORDER =================================================================
    public static final String ORDER_ID = "{orderId}";
    public static final String ORDER_CREATE_SUB_PATH = "";
    public static final String ORDER_CREATE_PATH = ORDER_BASE_PATH + ORDER_CREATE_SUB_PATH;
    public static final String ORDER_UPDATE_STATUS_SUB_PATH = "/" + ORDER_ID;
    public static final String ORDER_UPDATE_STATUS_PATH = ORDER_BASE_PATH + ORDER_UPDATE_STATUS_SUB_PATH;
    public static final String ORDER_GET_ALL_SHIPPING_SUB_PATH = "/shipping";
    public static final String ORDER_GET_ALL_SHIPPING_PATH = ORDER_BASE_PATH + ORDER_GET_ALL_SHIPPING_SUB_PATH;
    public static final String ORDER_GET_DETAILS_BY_ID_SUB_PATH = "/details/" + ORDER_ID;
    public static final String ORDER_GET_DETAILS_BY_ID_PATH = ORDER_BASE_PATH + ORDER_GET_DETAILS_BY_ID_SUB_PATH;
    public static final String ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_SUB_PATH = "/history/user/" + USER_ID;
    public static final String ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_PATH = ORDER_BASE_PATH + ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_SUB_PATH;
    public static final String ORDER_CREATE_REVIEW_SUB_PATH = "/rating/" + ORDER_ID;
    public static final String ORDER_CREATE_REVIEW_PATH = ORDER_BASE_PATH + ORDER_CREATE_REVIEW_SUB_PATH;

    // ENDPOINT URL TRANSACTION =================================================================
    public static final String TRANSACTION_ID = "{transId}";

    public static final String TRANSACTION_GET_BY_ID_PATH = TRANSACTION_BASE_PATH + "/" + TRANSACTION_ID;
    public static final String TRANSACTION_GET_ALL_PATH = TRANSACTION_BASE_PATH;
    public static final String TRANSACTION_UPDATE_BY_ID_SUB_PATH = "/" + TRANSACTION_ID;
    public static final String TRANSACTION_UPDATE_BY_ID_PATH = TRANSACTION_BASE_PATH + TRANSACTION_UPDATE_BY_ID_SUB_PATH;
    public static final String TRANSACTION_UPDATE_COMPLETE_SUB_PATH = "/complete/" + TRANSACTION_ID;
    public static final String TRANSACTION_UPDATE_COMPLETE_PATH = TRANSACTION_BASE_PATH + TRANSACTION_UPDATE_COMPLETE_SUB_PATH;
//    public static final String TRANSACTION_CREATE_PATH = TRANSACTION_BASE_PATH;


    // ENDPOINT URL AUTH =================================================================

    public static final String AUTH_SEND_OPT_SUB_PATH = "/send-opt";
    public static final String AUTH_SEND_OPT_PATH = AUTH_BASE_PATH + AUTH_SEND_OPT_SUB_PATH;
    public static final String AUTH_SEND_CODE_TO_REGISTER_SUB_PATH = "/register/send-code";
    public static final String AUTH_SEND_CODE_TO_REGISTER_PATH = AUTH_BASE_PATH + AUTH_SEND_CODE_TO_REGISTER_SUB_PATH;
    public static final String AUTH_SEND_CODE_TO_GET_PWD_SUB_PATH = "/password/send-code";
    public static final String AUTH_SEND_CODE_TO_GET_PWD_PATH = AUTH_BASE_PATH + AUTH_SEND_CODE_TO_GET_PWD_SUB_PATH;
    public static final String AUTH_RE_SEND_EMAIL_SUB_PATH = "/resend-email";
    public static final String AUTH_RE_SEND_EMAIL_PATH = AUTH_BASE_PATH + AUTH_RE_SEND_EMAIL_SUB_PATH;
    public static final String AUTH_REGISTER_SUB_PATH = "/register";
    public static final String AUTH_REGISTER_PATH = AUTH_BASE_PATH + AUTH_REGISTER_SUB_PATH;
    public static final String AUTH_LOGIN_SUB_PATH = "/login";
    public static final String AUTH_LOGIN_PATH = AUTH_BASE_PATH + AUTH_LOGIN_SUB_PATH;
    public static final String AUTH_VERIFY_EMAIL_SUB_PATH = "/verify";
    public static final String AUTH_VERIFY_EMAIL_PATH = AUTH_BASE_PATH + AUTH_VERIFY_EMAIL_SUB_PATH;
    public static final String AUTH_CHANGE_PASSWORD_SUB_PATH = "/change-password";
    public static final String AUTH_CHANGE_PASSWORD_PATH = AUTH_BASE_PATH + AUTH_CHANGE_PASSWORD_SUB_PATH;
    public static final String AUTH_EMPLOYEE_LOGIN_SUB_PATH = "/employee/login";
    public static final String AUTH_EMPLOYEE_LOGIN_PATH = AUTH_BASE_PATH + AUTH_EMPLOYEE_LOGIN_SUB_PATH;

}
