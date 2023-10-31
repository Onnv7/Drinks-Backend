package com.hcmute.drink.controller;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.impl.AuthServiceImpl;
import com.hcmute.drink.service.impl.EmployeeServiceImpl;
import com.hcmute.drink.service.impl.UserServiceImpl;
import com.hcmute.drink.utils.EmailUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = AUTH_CONTROLLER_TITLE)
@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH_BASE_PATH)
@Slf4j
public class AuthController {
    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;
    private final EmployeeServiceImpl employeeService;
    private final EmailUtils emailUtils;
    private final ModelMapper modelMapper;


    @Operation(summary = AUTH_LOGIN_SUM, description = AUTH_LOGIN_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.LOGIN, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(AUTH_LOGIN_SUB_PATH)
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
    @PostMapping(AUTH_REGISTER_SUB_PATH)
    public ResponseEntity<ResponseAPI> register(@RequestBody @Validated RegisterRequest body) {
        try {
            UserCollection data = modelMapper.map(body, UserCollection.class);
            RegisterResponse resDate = authService.registerUser(data);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resDate)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_RE_SEND_EMAIL_SUM, description = AUTH_RE_SEND_EMAIL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_CODE_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(AUTH_RE_SEND_EMAIL_SUB_PATH)
    public ResponseEntity<ResponseAPI> resendEmail(@RequestBody @Validated ResendEmailRequest body) {
        try {
            authService.resendCode(body.getEmail());
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.SEND_CODE_TO_EMAIL)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_SEND_CODE_TO_EMAIL_TO_REGISTER_SUM, description = AUTH_SEND_CODE_TO_EMAIL_TO_REGISTER_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_CODE_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(AUTH_SEND_CODE_TO_REGISTER_SUB_PATH)
    public ResponseEntity<ResponseAPI> sendCodeToRegister(@RequestBody @Validated SendCodeRequest body) {
        try {
            authService.sendCodeToRegister(body.getEmail());
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.SEND_CODE_TO_EMAIL)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_SEND_CODE_TO_EMAIL_TO_GET_PWD_SUM, description = AUTH_SEND_CODE_TO_EMAIL_TO_GET_PWD_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_CODE_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(AUTH_SEND_CODE_TO_GET_PWD_SUB_PATH)
    public ResponseEntity<ResponseAPI> sendCodeToGetPassword(@RequestBody @Validated SendCodeRequest body) {
        try {
            authService.sendCodeToGetPassword(body.getEmail());
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.SEND_CODE_TO_EMAIL)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Operation(summary = AUTH_SEND_OTP_TO_PHONE_NUMBER_SUM, description = AUTH_SEND_OTP_TO_PHONE_NUMBER_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_CODE_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(AUTH_SEND_OPT_SUB_PATH)
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

    @Operation(summary = AUTH_VERIFY_EMAIL_SUM, description = AUTH_VERIFY_EMAIL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.SEND_CODE_TO_EMAIL, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(AUTH_VERIFY_EMAIL_SUB_PATH)
    public ResponseEntity<ResponseAPI> verifyCodeByEmail(@RequestBody @Validated VerifyEmailRequest body) {
        try {
            log.debug("YOUR CODE: " + body.getCode());
            boolean result = authService.verifyCodeByEmail(body.getCode(), body.getEmail());
            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.EMAIL_VERIFIED)
                    .timestamp(new Date())
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_CHANGE_PASSWORD_SUM, description = AUTH_CHANGE_PASSWORD_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PatchMapping(AUTH_CHANGE_PASSWORD_SUB_PATH)
    public ResponseEntity<ResponseAPI> changePasswordWhenForgot(@RequestBody @Validated ChangePasswordRequest body) {
        try {
            userService.updatePasswordByEmail(body.getEmail(), body.getPassword());

            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.UPDATED)
                    .timestamp(new Date())
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_EMPLOYEE_LOGIN_SUM, description = AUTH_EMPLOYEE_LOGIN_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.LOGIN, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = AUTH_EMPLOYEE_LOGIN_SUB_PATH)
    public ResponseEntity<ResponseAPI> loginEmployee(@RequestBody @Validated EmployeeLoginRequest body, HttpServletResponse response) {
        try {
            EmployeeLoginResponse data = authService.attemptEmployeeLogin(body.getUsername(), body.getPassword());

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(data)
                    .message(SuccessConstant.LOGIN)
                    .build();


            HttpHeaders headers = new HttpHeaders();


            // TODO: kiem tra expire coookie
            headers.add(HttpHeaders.SET_COOKIE,"refreshToken=" + data.getRefreshToken() +"; Max-Age=604800; Path=/; Secure; HttpOnly");


            return new ResponseEntity<>(res, headers, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_REFRESH_TOKEN_SUM, description = AUTH_REFRESH_TOKEN_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET_NEW_TOKEN, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = AUTH_REFRESH_TOKEN_SUB_PATH)
    public ResponseEntity<ResponseAPI> refreshToken(@RequestBody @Validated RefreshTokenRequest body) {
        try {
            RefreshTokenResponse data = authService.refreshToken(body.getRefreshToken());

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(data)
                    .message(SuccessConstant.GET_NEW_TOKEN)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_REFRESH_EMPLOYEE_TOKEN_SUM, description = AUTH_REFRESH_EMPLOYEE_TOKEN_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET_NEW_TOKEN, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = AUTH_REFRESH_EMPLOYEE_TOKEN_SUB_PATH)
    public ResponseEntity<ResponseAPI> refreshEmployeeToken(@RequestBody @Validated RefreshEmployeeTokenRequest body) {
        try {
            RefreshEmployeeTokenResponse data = authService.refreshEmployeeToken(body.getRefreshToken());

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(data)
                    .message(SuccessConstant.GET_NEW_TOKEN)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
