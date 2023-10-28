package com.hcmute.drink.dto;

import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.common.ImageModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.hcmute.drink.constant.SwaggerConstant.CATEGORY_NAME_EX;
import static com.hcmute.drink.constant.SwaggerConstant.NOT_BLANK_DES;
@Data
public class CreateCategoryResponse {
    private String id;
    private String name;
    private ImageModel image;
    private boolean enabled;
}
