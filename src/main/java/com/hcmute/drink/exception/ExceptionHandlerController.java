package com.hcmute.drink.exception;

import com.hcmute.drink.constant.ErrorConstant.*;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if(throwable == null) {
            log.error("Throwable is null");
        }
        else if(error400.contains(throwable.getMessage())) {
            httpStatus = StatusCode.BAD_REQUEST;
        }

        ErrorResponse errorResponse = new ErrorResponse(new Date(), false, throwable.getMessage(), Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}