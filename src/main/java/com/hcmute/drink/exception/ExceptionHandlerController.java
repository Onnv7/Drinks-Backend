package com.hcmute.drink.exception;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.ErrorConstant.*;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    private static final List<String> error400= Arrays.asList(
            EMAIL_UNVERIFIED,
            REGISTERED_EMAIL,
            CREATED_FAILED,
            UPDATE_FAILED,
            CATEGORY_EXISTED,
            ACCOUNT_BLOCKED,
            OVER_FIVE_ADDRESS,
            ORDER_NOT_COMPLETED
    );
    private static final List<String> error404= Arrays.asList(
            USER_NOT_FOUND,
            EMPLOYEE_NOT_FOUND,
            NOT_FOUND,
            CATEGORY_NOT_FOUND,
            PRODUCT_NOT_FOUND
    );
    private static final List<String> error403= Arrays.asList(
            ACCESS_DENIED
    );
    private static final List<String> error401= Arrays.asList(
            INVALID_TOKEN, EXPIRED_TOKEN
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(new Date(), false, ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(errorResponse, StatusCode.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        Throwable throwable = ex.getCause();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String msg = "";

        // nếu không phải lỗi tự throws, là lỗi do server
        if(throwable == null) {
            Class<?> type = ex.getClass();
            if (ex instanceof AccessDeniedException) {
                httpStatus = StatusCode.FORBIDDEN;
                msg = ACCESS_DENIED;
            } else {
                log.error(ex.getMessage());
            }
        }
        // lỗi tự custom throw ra
        else {
            msg = throwable.getMessage();
            if(error400.contains(throwable.getMessage())) {
                httpStatus = StatusCode.BAD_REQUEST;
            } else if(error404.contains(throwable.getMessage())) {
                httpStatus = StatusCode.NOT_FOUND;
            } else if(error403.contains(throwable.getMessage())) {
                httpStatus = StatusCode.NOT_FOUND;
            } else if(throwable.getClass() == BadCredentialsException.class) {
                httpStatus = StatusCode.BAD_REQUEST;
            }
        }
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(new Date(), false, msg, Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}