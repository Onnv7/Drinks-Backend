package com.hcmute.drink.dto.request;

import com.hcmute.drink.enums.CategoryStatus;
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


    @Schema(example = CATEGORY_NAME_EX)
    @NotBlank
    private String name;

    @Schema(example = CATEGORY_STATUS_EX)
    @NotNull
    private CategoryStatus status;
}
