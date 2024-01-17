package com.hcmute.drink.dto.request;

import com.hcmute.drink.enums.EmployeeStatus;
import com.hcmute.drink.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateEmployeeRequest {

    @Schema(example = FIRST_NAME_EMPLOYEE_EX)
    @NotBlank
    private String firstName;

    @Schema(example = LAST_NAME_EMPLOYEE_EX)
    @NotBlank
    private String lastName;

    @Schema(example = BIRTH_DATE_EX)
    @NotNull
    private Date birthDate;

    @Schema(example = GENDER_EX)
    @NotNull
    private Gender gender;

    @Schema(example = EMPLOYEE_STATUS_EX)
    @NotNull
    private EmployeeStatus status;
}
