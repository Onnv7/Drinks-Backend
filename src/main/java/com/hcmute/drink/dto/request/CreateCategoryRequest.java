package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateCategoryRequest {
    @Schema()
    @NotNull
    private MultipartFile image;

    @Schema(example = CATEGORY_NAME_EX)
    @NotBlank
    private String name;
}
