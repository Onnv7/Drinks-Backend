package com.hcmute.drink.constant;

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
    public static final String AUTH_LOGIN_SUM = "User login to get token";
    public static final String AUTH_LOGIN_DES= "This endpoint returns a token";
    public static final String AUTH_REGISTER_SUM = "Register new user account with some information";
    public static final String AUTH_REGISTER_DES= "This endpoint returns a token and a user id";
    public static final String AUTH_VERIFY_EMAIL_SUM = "Verify confirmation code by email";
    public static final String AUTH_VERIFY_EMAIL_DES= NOTIFICATION_NOT_DATA;
    public static final String AUTH_RE_SEND_EMAIL_SUM = "Send email confirmation code a second time or later to update the Confirmation collection";
    public static final String AUTH_RE_SEND_EMAIL_DES = NOTIFICATION_NOT_DATA;
    public static final String AUTH_SEND_CODE_TO_EMAIL_TO_REGISTER_SUM = "Send email confirmation code to register account";
    public static final String AUTH_SEND_CODE_TO_EMAIL_TO_REGISTER_DES = NOTIFICATION_NOT_DATA;
    public static final String AUTH_SEND_CODE_TO_EMAIL_TO_GET_PWD_SUM = "Send email confirmation code to change password";
    public static final String AUTH_SEND_CODE_TO_EMAIL_TO_GET_PWD_DES = NOTIFICATION_NOT_DATA;
    public static final String AUTH_SEND_OTP_TO_PHONE_NUMBER_SUM = "Send OTP code to phone number (The function is under maintenance)";
    public static final String AUTH_SEND_OTP_TO_PHONE_NUMBER_DES= NOTIFICATION_NOT_DATA;
    public static final String AUTH_CHANGE_PASSWORD_SUM = "Change password by email";
    public static final String AUTH_CHANGE_PASSWORD_DES= NOTIFICATION_NOT_DATA;
    public static final String AUTH_EMPLOYEE_LOGIN_SUM = "Employee login to get token";
    public static final String AUTH_EMPLOYEE_LOGIN_DES = "This endpoint returns a token";

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
    public static final String USER_CHECK_EXISTED_BY_EMAIL_SUM = "Check user existence by email";
    public static final String USER_CHECK_EXISTED_BY_EMAIL_DES = "This endpoint returns user name or null";
    // For address =================================================================
    public static final String ADDRESS_CONTROLLER_TITLE = "ADDRESS MANAGEMENT";
    public static final String ADDRESS_ADD_ADDRESS_BY_ID_SUM = "Add new address for user";
    public static final String ADDRESS_ADD_ADDRESS_BY_ID_DES = "This endpoint returns a list of addresses";
    public static final String ADDRESS_UPDATE_ADDRESS_BY_ID_SUM = "Update address by address id";
    public static final String ADDRESS_UPDATE_ADDRESS_BY_ID_DES = "This endpoint returns new user's address after it were updated";
    public static final String ADDRESS_DELETE_ADDRESS_BY_ID_SUM = "Delete address by address id";
    public static final String ADDRESS_DELETE_ADDRESS_BY_ID_DES = NOTIFICATION_NOT_DATA;
    public static final String ADDRESS_GET_BY_USER_ID_SUM = "Get all addresses by user id";
    public static final String ADDRESS_GET_BY_USER_ID_DES = "This endpoint returns all addresses info of an user";
    public static final String ADDRESS_GET_DETAILS_BY_ID_SUM = "Get an address details by id";
    public static final String ADDRESS_GET_DETAILS_BY_ID_DES = "This endpoint returns an address details by id";


    // For product =================================================================

    public static final String PRODUCT_CONTROLLER_TITLE = "PRODUCT MANAGEMENT";
    public static final String PRODUCT_CREATE_SUM = "Create a new product";
    public static final String PRODUCT_CREATE_DES = "This endpoint returns new product after saved on database";
    public static final String PRODUCT_GET_BY_ID_SUM = "Get a product by product id";
    public static final String PRODUCT_GET_BY_ID_DES= "This endpoint returns a product's information";
    public static final String PRODUCT_GET_BY_CATEGORY_ID_SUM = "Get products by category id";
    public static final String PRODUCT_GET_BY_CATEGORY_ID_DES= "This endpoint returns products list";
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
    public static final String TRANSACTION_UPDATE_BY_ID_SUM = "Update transaction's information by transaction id after paid/canceled at vnpay page";
    public static final String TRANSACTION_UPDATE_BY_ID_DES= "This endpoint returns new transaction's information after it were updated";
    public static final String TRANSACTION_UPDATE_SUCCESS_STATUS_BY_ID_SUM = "Update transaction's status successfully by transaction id";
    public static final String TRANSACTION_UPDATE_SUCCESS_STATUS_BY_ID_DES= "This endpoint returns new transaction's information after it were updated";


    // For Order =============================================================

    public static final String ORDER_CONTROLLER_TITLE = "ORDER MANAGEMENT";

    public static final String ORDER_UPDATE_BY_ID_SUM = "Update order's information by order id";
    public static final String ORDER_UPDATE_BY_ID_DES= "This endpoint returns new order's information after it were updated";


    public static final String ORDER_UPDATE_EVENT_SUM = "Add an order event to the status line";
    public static final String ORDER_UPDATE_EVENT_DES = "This endpoint returns new order's information after it were updated";
    public static final String ORDER_CREATE_SHIPPING_SUM = "Create a new shipping order";
    public static final String ORDER_CREATE_SHIPPING_DES = "This endpoint returns new shipping order after saved on database";
    public static final String ORDER_GET_ALL_SUM = "Get all shipping orders for employee";
    public static final String ORDER_GET_ALL_DES= "This endpoint returns all orders";
    public static final String ORDER_GET_DETAILS_BY_ID_SUM = "Get a details order by order id";
    public static final String ORDER_GET_DETAILS_BY_ID_DES= "This endpoint returns a detail order";
    public static final String ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_SUM = "Get orders history by user id and order status";
    public static final String ORDER_GET_ORDERS_BY_USER_ID_AND_ORDER_STATUS_DES = "This endpoint returns orders history information";
    public static final String ORDER_CREATE_REVIEW_SUM = "Create review for order by order id";
    public static final String ORDER_CREATE_REVIEW_DES = "This endpoint returns new order information";

    // For Employee =============================================================
    public static final String EMPLOYEE_CONTROLLER_TITLE = "EMPLOYEE MANAGEMENT";
    public static final String EMPLOYEE_UPDATE_BY_ID_SUM = "Update employee's information by employee id";
    public static final String EMPLOYEE_UPDATE_BY_ID_DES= "This endpoint returns new employee's information after it were updated";

    public static final String EMPLOYEE_DELETE_BY_ID_SUM = "Delete a employee by employee id";
    public static final String EMPLOYEE_DELETE_BY_ID_DES= NOTIFICATION_NOT_DATA;
    public static final String EMPLOYEE_REGISTER_SUM = "Register new employee account with some information";
    public static final String EMPLOYEE_REGISTER_DES= NOTIFICATION_NOT_DATA;

    public static final String EMPLOYEE_GET_BY_ID_SUM = "Get a employee by employee id";
    public static final String EMPLOYEE_GET_BY_ID_DES= "This endpoint returns a employee's information";
    public static final String EMPLOYEE_GET_ALL_SUM = "Get all employees";
    public static final String EMPLOYEE_GET_ALL_DES= "This endpoint returns all employees";





    // For schema properties =====================================================

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
    public static final String PRODUCT_NAME_EX = "Coca cola";
    public static final String PRODUCT_PRICE_EX = "100000";

    public static final String PRODUCT_SIZE_EX = "Size L";
    public static final String PRODUCT_DESCRIPTION_EX = "Coca Cola is made from Pepsi";

    public static final String TOPPING_NAME_EX = "Ca cao" ;

    public static final String OBJECT_ID_EX = "3504503451AV8405345" ;
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
    public static final String RATING_EX = "4";
    public static final String CONTENT_EX = "It's so good";
    public static final String ORDER_STATUS_EX = "CREATED";
    public static final String ORDER_STATUS_DES_EX = "Order is created";
    public static final String PRODUCT_QUANTITY_EX = "3";
    public static final String PRODUCT_NOTE_EX = "This is note";
    public static final String INVOICE_NOTE_EX = "23654128";
    public static final String TOTAL_PAID_EX = "10234";
    public static final String LONGITUDE_EX = "10234";
    public static final String LATITUDE_EX = "10234";
    public static final String ADDRESS_NOTE_EX = "This is my house";
    public static final String RECIPIENT_NAME_EX = "Luu Bang";
    public static final String ORDER_NOTE_EX = "Quickly";
    public static final String PAYMENT_STATUS_EX = "PAID";
    public static final String PAYMENT_TYPE_EX = "BANKING";
    public static final String REVIEW_DES_EX = "Good service";
}
