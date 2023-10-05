package com.hcmute.drink.collection.embedded;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class AddressEmbedded {
    private String details;

    private double longitude;

    private double latitude;

    private String note;
    private String recipientName;

    private String phoneNumber;
}
