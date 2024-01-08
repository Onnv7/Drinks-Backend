package com.hcmute.drink.service;

import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.dto.request.RegisterRequest;

public interface IAuthService {
    LoginResponse userLogin(String email, String password);
    RegisterResponse registerUser(RegisterRequest body);
    void resendCode(String email);
    void sendCodeToRegister(String email);
    void sendCodeToGetPassword(String email);
    void verifyCodeByEmail(String code, String email);
    EmployeeLoginResponse attemptEmployeeLogin(String username, String password);
    RefreshTokenResponse refreshToken(String refreshToken);
    RefreshEmployeeTokenResponse refreshEmployeeToken(String refreshToken);
}