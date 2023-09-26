package com.hcmute.drink.exception;

import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.constant.ErrorConstant.*;
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
            USER_NOT_FOUND,
            REGISTERED_EMAIL,
            EMAIL_UNVERIFIED,
            NOT_FOUND
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
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
            }
        }
        // lỗi tự custom throw ra
        else {
            msg = throwable.getMessage();
            if(error400.contains(throwable.getMessage())) {
                httpStatus = StatusCode.BAD_REQUEST;
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(new Date(), false, msg, Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}