package com.hcmute.drink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyPhoneNumberRequest {
    @NotBlank
//    @Pattern(regexp = "^\\d{10}$")
    private String phoneNumber;
    @NotBlank
    private String message;
}
