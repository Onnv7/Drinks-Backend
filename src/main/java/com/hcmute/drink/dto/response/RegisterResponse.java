package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.drink.enums.Gender;
import com.hcmute.drink.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@NoArgsConstructor
public class RegisterResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;
}

