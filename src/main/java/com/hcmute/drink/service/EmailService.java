package com.hcmute.drink.service;

public interface EmailService {
    void sendHtmlVerifyEmail(String name, String to, String token);
}
