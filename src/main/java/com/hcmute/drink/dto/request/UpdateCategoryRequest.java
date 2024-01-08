package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateCategoryRequest {
    @Schema(description = OPTIONAL_DES)
    private MultipartFile image;


    @Schema(example = CATEGORY_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String name;

    @Schema(example = BOOLEAN_EX, description = NOT_NULL_DES)
    @NotNull
    private boolean enabled;
}
