package com.hcmute.drink.dto;

import com.hcmute.drink.common.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UpdateUserResponse {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Gender gender;
    private String phoneNumber;
}
