package com.hcmute.drink.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class ReviewModel {
    @Schema(example = RATING_EX, description = NOT_BLANK_DES)
    @NotBlank
    private double rating;

    @Schema(example = CONTENT_EX, description = OPTIONAL_DES)
    private String content;
}
