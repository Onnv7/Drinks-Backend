package com.hcmute.drink.constant;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

public class SwaggerConstant {
    public static final int TOPPING_PRICE_MIN = 5000;
    public static final int PRODUCT_PRICE_MIN = 5000;
    public static final int PASSWORD_LENGTH_MIN = 6;
    public static final int PASSWORD_LENGTH_MAX = 32;
    public static final int ORDER_QUANTITY_MIN = 1;
    public static final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    private static final String NOTIFICATION_NOT_DATA = "This endpoint returns message and no data";

    public static final String JSON_MEDIA_TYPE = "application/json";
    public static final String FORM_DATA_MEDIA_TYPE = "multipart/form-data";

    // For authentication =================================================================
    public static final String AUTH_CONTROLLER_TITLE = "AUTH MANAGEMENT";
    public static final String AUTH_LOGIN_SUM = "Login to get token";
    public static final String AUTH_LOGIN_DES= "This endpoint returns a token";
    public static final String AUTH_REGISTER_SUM = "Register new user account with some information";
    public static final String AUTH_REGISTER_DES= NOTIFICATION_NOT_DATA;
    public static final String AUTH_VERIFY_EMAIL_SUM = "Send email confirmation link for the first time";
    public static final String AUTH_VERIFY_EMAIL_DES= NOTIFICATION_NOT_DATA;
    public static final String AUTH_RE_SEND_EMAIL_SUM = "Send email confirmation link a second time or later to update the Confirmation collection";
    public static final String AUTH_RE_SEND_EMAIL_DES = NOTIFICATION_NOT_DATA;
    public static final String AUTH_SEND_CODE_TO_EMAIL_SUM = "Send email confirmation code";
    public static final String AUTH_SEND_CODE_TO_EMAIL_DES= NOTIFICATION_NOT_DATA;
    public static final String AUTH_SEND_OTP_TO_PHONE_NUMBER_SUM = "Send OTP code to phone number (The function is under maintenance)";
    public static final String AUTH_SEND_OTP_TO_PHONE_NUMBER_DES= NOTIFICATION_NOT_DATA;

    // For user =================================================================
    public static final String USER_CONTROLLER_TITLE = "USER MANAGEMENT";
    public static final String USER_GET_BY_ID_SUM = "Get a user by user id";
    public static final String USER_GET_BY_ID_DES= "This endpoint returns a user info";
    public static final String USER_GET_ALL_SUM = "Get all users";
    public static final String USER_GET_ALL_DES= "This endpoint returns all users info";
    public static final String USER_CHANGE_PWD_SUM = "Change password by user id";
    public static final String USER_CHANGE_PWD_DES= NOTIFICATION_NOT_DATA;
    public static final String USER_UPDATE_BY_ID_SUM = "Update user's profile by user id";
    public static final String USER_UPDATE_BY_ID_DES = "This endpoint returns new user's profile after it were updated";


    // For product =================================================================

    public static final String PRODUCT_CONTROLLER_TITLE = "PRODUCT MANAGEMENT";
    public static final String PRODUCT_CREATE_SUM = "Create a new product";
    public static final String PRODUCT_CREATE_DES = "This endpoint returns new product after saved on database";
    public static final String PRODUCT_GET_BY_ID_SUM = "Get a product by product id";
    public static final String PRODUCT_GET_BY_ID_DES= "This endpoint returns a product's information";
    public static final String PRODUCT_GET_ALL_SUM = "Get all products";
    public static final String PRODUCT_GET_ALL_DES= "This endpoint returns all products";
    public static final String PRODUCT_DELETE_BY_ID_SUM = "Delete a product by product id";
    public static final String PRODUCT_DELETE_BY_ID_DES= NOTIFICATION_NOT_DATA;
    public static final String PRODUCT_UPDATE_BY_ID_SUM = "Update product's information by product id";
    public static final String PRODUCT_UPDATE_BY_ID_DES= "This endpoint returns new product's information after it were updated";

    // For category =================================================================

    public static final String CATEGORY_CONTROLLER_TITLE = "CATEGORY MANAGEMENT";
    public static final String CATEGORY_CREATE_SUM = "Create a new category";
    public static final String CATEGORY_CREATE_DES = "This endpoint returns new category after saved on database";
    public static final String CATEGORY_GET_BY_ID_SUM = "Get a category by category id";
    public static final String CATEGORY_GET_BY_ID_DES= "This endpoint returns a category's information";

    public static final String CATEGORY_GET_ALL_SUM = "Get all categories";
    public static final String CATEGORY_GET_ALL_DES= "This endpoint returns all categories";
    public static final String CATEGORY_UPDATE_BY_ID_SUM = "Update category's information by category id";
    public static final String CATEGORY_UPDATE_BY_ID_DES= "This endpoint returns new category's information after it were updated";
    public static final String CATEGORY_DELETE_BY_ID_SUM = "Delete a category by category id";
    public static final String CATEGORY_DELETE_BY_ID_DES= NOTIFICATION_NOT_DATA;

    // For transaction =======================================================

    public static final String TRANSACTION_CONTROLLER_TITLE = "TRANSACTION MANAGEMENT";
    public static final String TRANSACTION_CREATE_SUM = "Create a new transaction";
    public static final String TRANSACTION_CREATE_DES = "This endpoint returns new transaction after saved on database";
    public static final String TRANSACTION_UPDATE_BY_ID_SUM = "Update transaction's information by category id";
    public static final String TRANSACTION_UPDATE_BY_ID_DES= "This endpoint returns new transaction's information after it were updated";




    // For Employee =============================================================
    public static final String EMPLOYEE_CONTROLLER_TITLE = "EMPLOYEE MANAGEMENT";
    public static final String EMPLOYEE_UPDATE_BY_ID_SUM = "Update employee's information by employee id";
    public static final String EMPLOYEE_UPDATE_BY_ID_DES= "This endpoint returns new employee's information after it were updated";

    public static final String EMPLOYEE_DELETE_BY_ID_SUM = "Delete a employee by employee id";
    public static final String EMPLOYEE_DELETE_BY_ID_DES= NOTIFICATION_NOT_DATA;
    public static final String EMPLOYEE_LOGIN_SUM = "Login to get token";
    public static final String EMPLOYEE_LOGIN_DES= "This endpoint returns a token";
    public static final String EMPLOYEE_REGISTER_SUM = "Register new employee account with some information";
    public static final String EMPLOYEE_REGISTER_DES= NOTIFICATION_NOT_DATA;

    public static final String EMPLOYEE_GET_BY_ID_SUM = "Get a employee by employee id";
    public static final String EMPLOYEE_GET_BY_ID_DES= "This endpoint returns a employee's information";
    public static final String EMPLOYEE_GET_ALL_SUM = "Get all employees";
    public static final String EMPLOYEE_GET_ALL_DES= "This endpoint returns all employees";





    // For schema properties

    public static final String NOT_EMPTY_DES = "Cannot be empty";
    public static final String NOT_BLANK_DES = "Cannot be blank";
    public static final String NOT_NULL_DES = "Cannot be null";
    public static final String OPTIONAL_DES = "Optional";
    public static final String REGEX_DES = "Write correct regex standards";

    public static final String MIN_VALUE_DES = "Min value: ";
    public static final String MIN_LENGTH_DES = "Minimum length: ";
    public static final String MAX_LENGTH_DES = "Minimum length: ";


    public static final String PASSWORD_EX = "123456";
    public static final String PASSWORD_DES = MIN_LENGTH_DES + PASSWORD_LENGTH_MIN + ", " + MAX_LENGTH_DES + PASSWORD_LENGTH_MAX;
    public static final String CATEGORY_NAME_EX = "Coffee";
    // Product
    public static final String PRODUCT_NAME_EX = "Coca cola";
    public static final String PRODUCT_PRICE_EX = "100000";

    public static final String PRODUCT_SIZE_EX = "Size L";
    public static final String PRODUCT_DESCRIPTION_EX = "Coca Cola is made from Pepsi";

    public static final String TOPPING_NAME_EX = "Ca cao" ;

    public static final String OBJECT_ID_EX = "35045034518405345" ;
    public static final String TOPPING_PRICE_EX = TOPPING_PRICE_MIN + 1000 + "" ;
    public static final String EMAIL_EX = "nva@gmail.com" ;
    public static final String FIRST_NAME_EX = "An" ;
    public static final String LAST_NAME_EX = "Nguyen" ;
    public static final String FIRST_NAME_EMPLOYEE_EX = "Tao" ;
    public static final String LAST_NAME_EMPLOYEE_EX = "Thao" ;
    public static final String BIRTH_DATE_EX = "2002-01-01";
    public static final String GENDER_EX = "FEMALE";
    public static final String PHONE_NUMBER_EX = "0123456789";
    public static final String CODE_EX = "0000";
    public static final String VERIFY_NUMBER_MSG_EX = "Your authentication code is: 0000";
    public static final String USERNAME_EMPLOYEE_EX = "nva6112002";
    public static final String PASSWORD_EMPLOYEE_EX = "112233";
}
