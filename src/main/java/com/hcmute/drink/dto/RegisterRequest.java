package com.hcmute.drink.dto;

import com.hcmute.drink.common.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RegisterRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Date birthDate;

    @NotNull
    private Gender gender;

    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private String phoneNumber;
}
