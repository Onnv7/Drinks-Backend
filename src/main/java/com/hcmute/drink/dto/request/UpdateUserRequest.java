package com.hcmute.drink.dto.request;

import com.hcmute.drink.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.*;


@Data
@Builder
public class UpdateUserRequest {
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

//    @Schema(example = PHONE_NUMBER_EX, description = REGEX_DES)
//    @NotBlank
//    @Pattern(regexp = PHONE_NUMBER_REGEX)
//    private String phoneNumber;
}
