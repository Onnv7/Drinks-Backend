package com.hcmute.drink.constant;

public class SecurityConstant {
    public static final String SET_ADMIN_ROLE = "hasRole('ADMIN')";
    public static final String SET_USER_ROLE = "hasRole('USER')";

    public static final String USER_BASE_PATH = "/api/user";
    public static final String AUTH_BASE_PATH = "/api/auth";
    public static final String CATEGORY_BASE_PATH = "/api/category";
    public static final String PRODUCT_BASE_PATH = "/api/product";

    public static final String USER_ALL_PATH = USER_BASE_PATH + "/**";
    public static final String AUTH_ALL_PATH = AUTH_BASE_PATH + "/**";
    public static final String CATEGORY_ALL_PATH = CATEGORY_BASE_PATH + "/**";
    public static final String PRODUCT_ALL_PATH = PRODUCT_BASE_PATH + "/**";

    public static final String[] AUTH_WHITELIST = {
            "/openapi/**",
            "/v3/api-docs/**",
            "/openapi/swagger-config/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            USER_ALL_PATH,
            AUTH_ALL_PATH,
            CATEGORY_ALL_PATH,
            "/api/product/**",
            "/api/test/**"
    };
    public static final String[] USER_PATH = {
//            "/api/user/**",
            "/api/product"
    };
    public static final String[] ADMIN_PATH = {
    };
}
