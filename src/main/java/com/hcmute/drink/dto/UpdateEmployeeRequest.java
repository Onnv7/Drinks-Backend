package com.hcmute.drink.dto;

import com.hcmute.drink.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateEmployeeRequest {

    @Schema(example = FIRST_NAME_EMPLOYEE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String firstName;

    @Schema(example = LAST_NAME_EMPLOYEE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String lastName;

    @Schema(example = BIRTH_DATE_EX, description = NOT_NULL_DES)
    @NotNull
    private Date birthDate;

    @Schema(example = GENDER_EX, description = NOT_NULL_DES)
    @NotNull
    private Gender gender;

    @Schema(example = BOOLEAN_EX, description = NOT_NULL_DES)
    @NotNull
    private boolean enabled;
}
