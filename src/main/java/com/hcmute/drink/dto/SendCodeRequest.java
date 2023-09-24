package com.hcmute.drink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendCodeRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String code;
}
