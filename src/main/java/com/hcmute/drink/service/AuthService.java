package com.hcmute.drink.service;

import com.hcmute.drink.dto.LoginResponse;
import com.hcmute.drink.dto.RegisterRequest;

public interface AuthService {
    public LoginResponse attemptLogin(String email, String password) throws Exception;
    public void registerUser(RegisterRequest body) throws Exception;
    public boolean verifyEmail(String token);
    public void resendTokenToEmail(String email) throws Exception;
    public void sendCodeToEmail(String email, String code) throws Exception;
    public void sendMessageToPhoneNumber(String phoneNumber, String text) throws Exception;
}
