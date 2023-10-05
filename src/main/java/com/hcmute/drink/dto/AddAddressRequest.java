package com.hcmute.drink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class AddAddressRequest {
    @Schema(example = CATEGORY_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String details;

    @Schema(example = LONGITUDE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private double longitude;

    @Schema(example = LATITUDE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private double latitude;

    @Schema(example = ADDRESS_NOTE_EX, description = OPTIONAL_DES)
    private String note;

    @Schema(example = RECIPIENT_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String recipientName;

    @Schema(example = PHONE_NUMBER_EX, description = REGEX_DES)
    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEX)
    private String phoneNumber;
}
