package com.hcmute.drink.dto;

import com.hcmute.drink.common.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
public class UpdateUserRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Date birthDate;

    @NotNull
    private Gender gender;

    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private String phoneNumber;
}
