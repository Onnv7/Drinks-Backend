package com.hcmute.drink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class VerifyEmailRequest {
    @Schema(example = EMAIL_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String email;
    @Schema(example = CODE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String code;
}
