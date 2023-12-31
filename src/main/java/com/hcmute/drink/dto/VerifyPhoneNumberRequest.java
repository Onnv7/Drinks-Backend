package com.hcmute.drink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class VerifyPhoneNumberRequest {
    @Schema(example = PHONE_NUMBER_EX, description = REGEX_DES)
    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEX)
    private String phoneNumber;

    @Schema(example = VERIFY_NUMBER_MSG_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String message;
}
