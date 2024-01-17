package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.NOT_BLANK_DES;
import static com.hcmute.drink.constant.SwaggerConstant.REFRESH_TOKEN_EX;

@Data
public class RefreshTokenRequest {

    @Schema(example = REFRESH_TOKEN_EX)
    @NotBlank
    private String refreshToken;
}
