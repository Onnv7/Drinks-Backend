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
public class UserToken implements Serializable {
    private String userId;
    private String refreshToken;
    private boolean isUsed;
}
