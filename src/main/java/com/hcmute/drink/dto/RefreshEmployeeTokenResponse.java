package com.hcmute.drink.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshEmployeeTokenResponse {
    private String refreshToken;
    private String accessToken;
}
