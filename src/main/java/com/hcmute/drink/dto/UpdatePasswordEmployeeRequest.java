package com.hcmute.drink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.PASSWORD_LENGTH_MAX;

@Data
public class UpdatePasswordEmployeeRequest {
    @Schema(example = PASSWORD_EX, description = PASSWORD_DES)
    @NotBlank
    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX)
    private String password;
}
