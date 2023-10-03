package com.hcmute.drink.service;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.dto.LoginResponse;
import com.hcmute.drink.dto.RegisterResponse;

public interface AuthService {
    public LoginResponse attemptLogin(String email, String password) throws Exception;
    public RegisterResponse registerUser(UserCollection body) throws Exception;
//    public void resendTokenToEmail(String email) throws Exception;
    public void sendCodeToRegister(String email) throws Exception;
    public void sendMessageToPhoneNumber(String phoneNumber, String text) throws Exception;
}
