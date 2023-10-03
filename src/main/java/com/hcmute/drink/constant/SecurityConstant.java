package com.hcmute.drink.constant;

public class SecurityConstant {
    public static final String SET_ADMIN_ROLE = "hasRole('ADMIN')";
    public static final String SET_USER_ROLE = "hasRole('USER')";

    // =================================================
    public static final String USER_BASE_PATH = "/api/user";
    public static final String AUTH_BASE_PATH = "/api/auth";
    public static final String CATEGORY_BASE_PATH = "/api/category";
    public static final String PRODUCT_BASE_PATH = "/api/product";
    public static final String EMPLOYEE_BASE_PATH = "/api/employee";
    public static final String ORDER_BASE_PATH = "/api/order";
    public static final String TRANSACTION_BASE_PATH = "/api/transaction";
    // =================================================
    public static final String USER_ALL_PATH = USER_BASE_PATH + "/**";
    public static final String AUTH_ALL_PATH = AUTH_BASE_PATH + "/**";
    public static final String CATEGORY_ALL_PATH = CATEGORY_BASE_PATH + "/**";
    public static final String PRODUCT_ALL_PATH = PRODUCT_BASE_PATH + "/**";
    public static final String ORDER_ALL_PATH = PRODUCT_BASE_PATH + "/**";

    // ENDPOINT URL USER =================================================================

    private static final String USER_ID = "{userId}";

    public static final String USER_UPDATE_BY_ID_PATH = USER_BASE_PATH + "/update/" + USER_ID;
    public static final String USER_CHANGE_PASSWORD_PATH = USER_BASE_PATH + "/change-password/" + USER_ID;
    public static final String USER_GET_BY_ID_PATH = USER_BASE_PATH + "/" + USER_ID;
    public static final String USER_GET_ALL_PATH = USER_BASE_PATH;

    // ENDPOINT URL PRODUCT =================================================================
    public static final String PRODUCT_ID = "{productId}";

    public static final String PRODUCT_GET_BY_ID_PATH = PRODUCT_BASE_PATH + "/" + PRODUCT_ID;
    public static final String PRODUCT_GET_ALL_PATH = PRODUCT_BASE_PATH;
    public static final String PRODUCT_UPDATE_BY_ID_PATH = PRODUCT_BASE_PATH + "/" + PRODUCT_ID;
    public static final String PRODUCT_DELETE_BY_ID_PATH = PRODUCT_BASE_PATH + "/" + PRODUCT_ID;
    public static final String PRODUCT_CREATE_PATH = PRODUCT_BASE_PATH;


    // ENDPOINT URL EMPLOYEE =================================================================
    public static final String EMPLOYEE_ID = "{employeeId}";

    public static final String EMPLOYEE_GET_BY_ID_PATH = EMPLOYEE_BASE_PATH + "/" + EMPLOYEE_ID;
    public static final String EMPLOYEE_GET_ALL_PATH = EMPLOYEE_BASE_PATH;
    public static final String EMPLOYEE_UPDATE_BY_ID_PATH = EMPLOYEE_BASE_PATH + "/" + EMPLOYEE_ID;
    public static final String EMPLOYEE_DELETE_BY_ID_PATH = EMPLOYEE_BASE_PATH + "/" + EMPLOYEE_ID;
    public static final String EMPLOYEE_REGISTER_PATH = EMPLOYEE_BASE_PATH + "/register";
    public static final String EMPLOYEE_LOGIN_PATH = EMPLOYEE_BASE_PATH + "/login";

    // ENDPOINT URL CATEGORY =================================================================
    public static final String CATEGORY_ID = "{categoryId}";

    public static final String CATEGORY_GET_BY_ID_PATH = CATEGORY_BASE_PATH + "/" + CATEGORY_ID;
    public static final String CATEGORY_GET_ALL_PATH = CATEGORY_BASE_PATH;
    public static final String CATEGORY_UPDATE_BY_ID_PATH = CATEGORY_BASE_PATH + "/update/" + CATEGORY_ID;
    public static final String CATEGORY_DELETE_BY_ID_PATH = CATEGORY_BASE_PATH + "/" + CATEGORY_ID;
    public static final String CATEGORY_CREATE_PATH = CATEGORY_BASE_PATH;

    // ENDPOINT URL ORDER =================================================================
    public static final String ORDER_ID = "{orderId}";
    public static final String ORDER_CREATE_PATH = ORDER_BASE_PATH;
    public static final String ORDER_UPDATE_STATUS_PATH = ORDER_BASE_PATH + "/" + ORDER_ID;

    // ENDPOINT URL TRANSACTION =================================================================
    public static final String TRANSACTION_ID = "{transId}";

    public static final String TRANSACTION_GET_BY_ID_PATH = TRANSACTION_BASE_PATH + "/" + TRANSACTION_ID;
    public static final String TRANSACTION_GET_ALL_PATH = TRANSACTION_BASE_PATH;
    public static final String TRANSACTION_UPDATE_BY_ID_PATH = TRANSACTION_BASE_PATH + "/update/" + TRANSACTION_ID;
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



//    public static final String[] AUTH_WHITELIST = {
//            "/openapi/**",
//            "/v3/api-docs/**",
//            "/openapi/swagger-config/**",
//            "/v3/api-docs.yaml",
//            "/swagger-ui/**",
//            "/swagger-ui.html",
//            USER_ALL_PATH,
//            AUTH_ALL_PATH,
//            CATEGORY_ALL_PATH,
//            "/api/test/**",
//            PRODUCT_GET_ALL_PATH, PRODUCT_GET_BY_ID_PATH,
//            EMPLOYEE_LOGIN_PATH,
//            CATEGORY_GET_ALL_PATH, CATEGORY_GET_BY_ID_PATH,
//            AUTH_ALL_PATH
//    };


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

    };
    public static final String[] POST_AUTH_WHITELIST = {
            "/refund",
            "/IPN/**", "/api/order",
            EMPLOYEE_LOGIN_PATH,
            AUTH_SEND_OPT_PATH, AUTH_SEND_CODE_TO_REGISTER_PATH, AUTH_RE_SEND_EMAIL_PATH,
                AUTH_REGISTER_PATH, AUTH_LOGIN_PATH, AUTH_VERIFY_EMAIL_PATH, AUTH_SEND_CODE_TO_GET_PWD_PATH
    };

    public static final String[] PATCH_AUTH_WHITELIST = {
            AUTH_CHANGE_PASSWORD_PATH
    };
    // Only USER =================================================================
    public static final String[] PATCH_USER_PATH = {
            USER_CHANGE_PASSWORD_PATH
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
            TRANSACTION_UPDATE_BY_ID_PATH
    };
    public static final String[] PATCH_ADMIN_EMPLOYEE_PATH = {
            ORDER_UPDATE_STATUS_PATH
    };


    // ADMIN + USER =================================================================
    public static final String[] GET_ADMIN_USER_PATH = {
            USER_GET_BY_ID_PATH,
    };

    public static final String[] POST_ADMIN_USER_PATH = {
//            TRANSACTION_CREATE_PATH,
            ORDER_CREATE_PATH,
    };

    public static final String[] PUT_ADMIN_USER_PATH = {
            USER_UPDATE_BY_ID_PATH,
    };




}
