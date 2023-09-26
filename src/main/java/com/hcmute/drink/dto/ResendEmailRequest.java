package com.hcmute.drink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.EMAIL_EX;
import static com.hcmute.drink.constant.SwaggerConstant.NOT_BLANK_DES;

@Data
public class ResendEmailRequest {
    @Schema(example = EMAIL_EX, description = NOT_BLANK_DES)
    @Email
    @NotBlank
    private String email;
}
