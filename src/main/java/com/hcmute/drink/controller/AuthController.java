package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.model.ApiResponse;
import com.hcmute.drink.service.impl.AuthServiceImpl;
import com.hcmute.drink.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthServiceImpl authService;
    private final EmailUtils emailService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Validated LoginRequest body) {
        try {
            LoginResponse data = authService.attemptLogin(body.getEmail(), body.getPassword());
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .success(true)
                    .message(SuccessConstant.LOGIN)
                    .data(data)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Validated RegisterRequest body) {
        try {
            authService.registerUser(body);
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .success(true)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        try {
            authService.verifyEmail(token);
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.EMAIL_VERIFIED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/resend-email")
    public ResponseEntity<ApiResponse> resendEmail(@RequestBody ResendEmailRequest body) {
        try {
            authService.resendTokenToEmail(body.getEmail());
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.SEND_TOKEN_TO_EMAIL)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse> sendCode(@RequestBody SendCodeRequest body) {
        try {
            authService.sendCodeToEmail(body.getEmail(), body.getCode());
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.SEND_TOKEN_TO_EMAIL)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/send-opt")
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody @Validated VerifyPhoneNumberRequest body) {
        try {
            log.info("YOUR CODE: " + body.getMessage());
//            authService.sendMessageToPhoneNumber(body.getPhoneNumber(), body.getMessage());
            ApiResponse res = ApiResponse.builder()
                    .success(true)
                    .message(SuccessConstant.SEND_OTP)
                    .timestamp(new Date())
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
