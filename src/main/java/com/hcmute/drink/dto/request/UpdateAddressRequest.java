package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateAddressRequest {
    @Schema(example = CATEGORY_NAME_EX)
    @NotBlank
    private String details;

    @Schema(example = LONGITUDE_EX)
    @NotNull
    private double longitude;

    @Schema(example = LATITUDE_EX)
    @NotNull
    private double latitude;

    @Schema(example = ADDRESS_NOTE_EX)
    private String note;

    @Schema(example = RECIPIENT_NAME_EX)
    @NotBlank
    private String recipientName;

    @Schema(example = BOOLEAN_EX)
    @NotNull
    private boolean isDefault;

    @Schema(example = PHONE_NUMBER_EX)
    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEX)
    private String phoneNumber;
}
