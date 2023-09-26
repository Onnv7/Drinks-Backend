package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.impl.AuthServiceImpl;
import com.hcmute.drink.utils.EmailUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = AUTH_CONTROLLER_TITLE)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthServiceImpl authService;
    private final EmailUtils emailService;


    @Operation(summary = AUTH_LOGIN_SUM, description = AUTH_LOGIN_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.LOGIN, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping("/login")
    public ResponseEntity<ResponseAPI> login(@RequestBody @Validated LoginRequest body) {
        try {
            LoginResponse data = authService.attemptLogin(body.getEmail(), body.getPassword());
            ResponseAPI res = ResponseAPI.builder()
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

    @Operation(summary = AUTH_REGISTER_SUM, description = AUTH_REGISTER_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping("/register")
    public ResponseEntity<ResponseAPI> register(@RequestBody @Validated RegisterRequest body) {
        try {
            authService.registerUser(body);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .success(true)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_VERIFY_EMAIL_SUM, description = AUTH_VERIFY_EMAIL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping("/verify-email")
    public ResponseEntity<ResponseAPI> verifyEmail(@RequestParam("token") String token) {
        try {
            authService.verifyEmail(token);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.EMAIL_VERIFIED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_RE_SEND_EMAIL_SUM, description = AUTH_RE_SEND_EMAIL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_TOKEN_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping("/resend-email")
    public ResponseEntity<ResponseAPI> resendEmail(@RequestBody ResendEmailRequest body) {
        try {
            authService.resendTokenToEmail(body.getEmail());
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.SEND_TOKEN_TO_EMAIL)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_SEND_CODE_TO_EMAIL_SUM, description = AUTH_SEND_CODE_TO_EMAIL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_TOKEN_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping("/send-code")
    public ResponseEntity<ResponseAPI> sendCode(@RequestBody SendCodeRequest body) {
        try {
            authService.sendCodeToEmail(body.getEmail(), body.getCode());
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.SEND_TOKEN_TO_EMAIL)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Operation(summary = AUTH_SEND_OTP_TO_PHONE_NUMBER_SUM, description = AUTH_SEND_OTP_TO_PHONE_NUMBER_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_TOKEN_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping("/send-opt")
    public ResponseEntity<ResponseAPI> sendOTP(@RequestBody @Validated VerifyPhoneNumberRequest body) {
        try {
            log.info("YOUR CODE: " + body.getMessage());
//            authService.sendMessageToPhoneNumber(body.getPhoneNumber(), body.getMessage());
            ResponseAPI res = ResponseAPI.builder()
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
