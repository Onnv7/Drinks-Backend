package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
@NoArgsConstructor
public class UpdateUserResponse {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Gender gender;

//    @Schema(example = PHONE_NUMBER_EX, description = REGEX_DES)
//    @NotBlank
//    @Pattern(regexp = PHONE_NUMBER_REGEX)
//    private String phoneNumber;
}
