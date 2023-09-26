package com.hcmute.drink.dto;

import com.hcmute.drink.common.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
@Builder
public class RegisterRequest {
    @Schema(example = EMAIL_EX, description = NOT_BLANK_DES)
    @Email
    @NotBlank
    private String email;

    @Schema(example = PASSWORD_EX, description = PASSWORD_DES)
    @NotBlank
    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX)
    private String password;

    @Schema(example = FIRST_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String firstName;

    @Schema(example = LAST_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String lastName;

    @Schema(example = BIRTH_DATE_EX, description = NOT_NULL_DES)
    @NotNull
    private Date birthDate;

    @Schema(example = GENDER_EX, description = NOT_NULL_DES)
    @NotNull
    private Gender gender;

    @Schema(example = PHONE_NUMBER_EX, description = REGEX_DES)
    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEX)
    private String phoneNumber;
}
