package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateReviewRequest {
    @Schema(example = RATING_EX)
    @NotNull
    private int rating;

    @Schema(example = REVIEW_DES_EX)
    private String content;
}
