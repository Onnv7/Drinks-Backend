package com.hcmute.drink.model.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeToken implements Serializable {
    private String employeeId;
    private String refreshToken;
    private boolean isUsed;
}
