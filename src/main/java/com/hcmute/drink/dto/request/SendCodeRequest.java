package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class SendCodeRequest {
    @Schema(example = EMAIL_EX, description = NOT_BLANK_DES)
    @Email
    @NotBlank
    private String email;

}
