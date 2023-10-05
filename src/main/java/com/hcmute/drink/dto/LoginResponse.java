package com.hcmute.drink.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String userId;
    private final String accessToken;
}
