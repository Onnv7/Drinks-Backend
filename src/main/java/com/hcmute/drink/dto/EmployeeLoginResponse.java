package com.hcmute.drink.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeLoginResponse {
    private final String employeeId;
    private final String accessToken;
    private final String refreshToken;
}
