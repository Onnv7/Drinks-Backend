package com.hcmute.drink.exception;

import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.ErrorResponse;
import com.hcmute.drink.model.FieldError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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
    @Value("spring.profile.active")
    private String environment;
    private String dev = "dev";
    private String prod = "prod";

    private static final List<String> error404= Arrays.asList(
            USER_NOT_FOUND,
            EMPLOYEE_NOT_FOUND,
            NOT_FOUND,
            CATEGORY_NOT_FOUND,
            PRODUCT_NOT_FOUND
    );
    private static final List<String> error400= Arrays.asList(
            REGISTERED_EMAIL
    );
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ex.printStackTrace();

        ErrorResponse res = ErrorResponse.builder()
                .message(ErrorConstant.REQUEST_BODY_INVALID)
                .stack(environment.equals(dev) ? Arrays.toString(ex.getStackTrace()) : null)
                .build();
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        ErrorResponse res = ErrorResponse.builder()
                .message(ex.getMessage())
                .stack(environment.equals(dev) ? Arrays.toString(ex.getStackTrace()) : null)
                .build();
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ex.printStackTrace();
        ErrorResponse res = ErrorResponse.builder()
                .message(ex.getMessage())
                .stack(environment.equals(dev) ? Arrays.toString(ex.getStackTrace()) : null)
                .build();
        return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ex.printStackTrace();
        if(error404.contains(ex.getMessage())) {
            httpStatus = StatusCode.NOT_FOUND;
        } else if(error400.contains(ex.getMessage())) {
            httpStatus = StatusCode.BAD_REQUEST;
        }
        ErrorResponse res = ErrorResponse.builder()
                .message(ex.getMessage())
                .stack(environment.equals(dev) ? Arrays.toString(ex.getStackTrace()) : null)
                .build();
        return new ResponseEntity<>(res, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ex.printStackTrace();

        ErrorResponse res = ErrorResponse.builder()
                .message(ErrorConstant.REQUEST_BODY_INVALID)
                .details(
                        ex.getFieldErrors().stream()
                                .map(
                                        it-> FieldError.builder()
                                                .field(it.getField())
                                                .valueReject(it.getRejectedValue())
                                                .validate(it.getDefaultMessage())
                                                .build()
                                ).toList()
                )
                .build();
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}