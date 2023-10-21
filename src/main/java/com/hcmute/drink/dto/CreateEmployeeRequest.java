package com.hcmute.drink.dto;

import com.hcmute.drink.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateEmployeeRequest {
    @Schema(example = USERNAME_EMPLOYEE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String username;

    @Schema(example = PASSWORD_EMPLOYEE_EX, description = PASSWORD_DES)
    @NotBlank
    @Size(min = PASSWORD_LENGTH_MIN, max = PASSWORD_LENGTH_MAX)
    private String password;

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
}
