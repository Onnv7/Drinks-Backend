package com.hcmute.drink.controller;

import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;

import com.hcmute.drink.dto.request.*;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.IAuthService;
import com.hcmute.drink.service.database.implement.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final IAuthService authService;
    private final UserService userService;


    @Operation(summary = AUTH_LOGIN_SUM)
    @PostMapping(POST_AUTH_LOGIN_SUB_PATH)
    public ResponseEntity<ResponseAPI> userLogin(@RequestBody @Valid LoginRequest body) {
        LoginResponse data = authService.userLogin(body.getEmail(), body.getPassword());
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .success(true)
                .message(SuccessConstant.LOGIN)
                .data(data)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = AUTH_REGISTER_SUM)
    @PostMapping(POST_AUTH_REGISTER_SUB_PATH)
    public ResponseEntity<ResponseAPI> registerUser(@RequestBody @Valid RegisterRequest body) {
        RegisterResponse resDate = authService.registerUser(body);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resDate)
                .message(SuccessConstant.CREATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.CREATED);


    }

    @Operation(summary = AUTH_RE_SEND_EMAIL_SUM)
    @PostMapping(POST_AUTH_RE_SEND_EMAIL_SUB_PATH)
    public ResponseEntity<ResponseAPI> resendEmail(@RequestBody @Valid ResendEmailRequest body) {
        authService.resendCode(body.getEmail());
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.SEND_CODE_TO_EMAIL)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = AUTH_SEND_CODE_TO_EMAIL_TO_REGISTER_SUM)
    @PostMapping(POST_AUTH_SEND_CODE_TO_REGISTER_SUB_PATH)
    public ResponseEntity<ResponseAPI> sendCodeToRegister(@RequestBody @Valid SendCodeRequest body) {

        authService.sendCodeToRegister(body.getEmail());
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.SEND_CODE_TO_EMAIL)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = AUTH_SEND_CODE_TO_EMAIL_TO_GET_PWD_SUM)
    @PostMapping(POST_AUTH_SEND_CODE_TO_GET_PWD_SUB_PATH)
    public ResponseEntity<ResponseAPI> sendCodeToGetPassword(@RequestBody @Valid SendCodeRequest body) {

        authService.sendCodeToGetPassword(body.getEmail());
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.SEND_CODE_TO_EMAIL)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }


//    @Operation(summary = AUTH_SEND_OTP_TO_PHONE_NUMBER_SUM)
//    @PostMapping(POST_AUTH_SEND_OPT_SUB_PATH)
//    public ResponseEntity<ResponseAPI> sendOTP(@RequestBody @Validated VerifyPhoneNumberRequest body) {
//        try {
//            log.info("YOUR CODE: " + body.getMessage());
////            authService.sendMessageToPhoneNumber(body.getPhoneNumber(), body.getMessage());
//            ResponseAPI res = ResponseAPI.builder()
//                    .message(SuccessConstant.SEND_OTP)
//                    .timestamp(new Date())
//                    .build();
//            return new ResponseEntity<>(res, StatusCode.OK);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Operation(summary = AUTH_VERIFY_EMAIL_SUM)
    @PostMapping(POST_AUTH_VERIFY_EMAIL_SUB_PATH)
    public ResponseEntity<ResponseAPI> verifyCodeByEmail(@RequestBody @Valid VerifyEmailRequest body) {

        log.debug("YOUR CODE: " + body.getCode());
        authService.verifyCodeByEmail(body.getCode(), body.getEmail());
        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.EMAIL_VERIFIED)
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    // TODO: xem lai cho userService trong auth controller
    @Operation(summary = AUTH_CHANGE_PASSWORD_SUM)
    @PatchMapping(PATCH_AUTH_CHANGE_PASSWORD_SUB_PATH)
    public ResponseEntity<ResponseAPI> changePasswordForgot(@RequestBody @Valid ChangePasswordRequest body) {
        try {
            userService.changePasswordForgot(body.getEmail(), body.getPassword());

            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.UPDATED)
                    .timestamp(new Date())
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = AUTH_EMPLOYEE_LOGIN_SUM)
    @PostMapping(path = POST_AUTH_EMPLOYEE_LOGIN_SUB_PATH)
    public ResponseEntity<ResponseAPI> loginEmployee(@RequestBody @Valid EmployeeLoginRequest body) {
        EmployeeLoginResponse data = authService.attemptEmployeeLogin(body.getUsername(), body.getPassword());

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(data)
                .message(SuccessConstant.LOGIN)
                .build();


        HttpHeaders headers = new HttpHeaders();


        // TODO: kiem tra expire coookie
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + data.getRefreshToken() + "; Max-Age=604800; Path=/; Secure; HttpOnly");


        return new ResponseEntity<>(res, headers, StatusCode.OK);

    }

    @Operation(summary = AUTH_REFRESH_TOKEN_SUM)
    @PostMapping(path = POST_AUTH_REFRESH_TOKEN_SUB_PATH)
    public ResponseEntity<ResponseAPI> refreshToken(@RequestBody @Valid RefreshTokenRequest body) {
        RefreshTokenResponse data = authService.refreshToken(body.getRefreshToken());

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(data)
                .message(SuccessConstant.GET_NEW_TOKEN)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = AUTH_REFRESH_EMPLOYEE_TOKEN_SUM)
    @PostMapping(path = POST_AUTH_REFRESH_EMPLOYEE_TOKEN_SUB_PATH)
    public ResponseEntity<ResponseAPI> refreshEmployeeToken(
            @RequestBody(required = false)  RefreshEmployeeTokenRequest body, HttpServletRequest request
            , @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        System.out.println(request);
        if (refreshToken == null &&  body == null) {
            throw new CustomException(ErrorConstant.INVALID_TOKEN);
        }
        RefreshEmployeeTokenResponse data = authService.refreshEmployeeToken( body == null ? refreshToken : body.getRefreshToken());

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(data)
                .message(SuccessConstant.GET_NEW_TOKEN)
                .build();

        HttpHeaders headers = new HttpHeaders();


        // TODO: kiem tra expire coookie
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + data.getRefreshToken() + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return new ResponseEntity<>(res, headers, StatusCode.OK);
    }
}
