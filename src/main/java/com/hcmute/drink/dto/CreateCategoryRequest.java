package com.hcmute.drink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateCategoryRequest {
    @Schema(description = NOT_NULL_DES)
    @NotNull
    private MultipartFile image;

    @Schema(example = CATEGORY_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String name;
}
