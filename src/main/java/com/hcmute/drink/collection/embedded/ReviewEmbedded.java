package com.hcmute.drink.collection.embedded;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class ReviewEmbedded {
    private int rating;

    private String content;
}
