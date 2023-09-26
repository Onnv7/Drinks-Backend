package com.hcmute.drink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class ChangePasswordRequest {
    @Schema(example = PASSWORD_EX, description = MIN_LENGTH_DES + PASSWORD_LENGTH_MIN + ", " + MAX_LENGTH_DES + PASSWORD_LENGTH_MAX)
    @NotBlank
    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX)
    private String password;
}
