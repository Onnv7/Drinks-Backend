package com.hcmute.drink.dto;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.common.ToppingModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateProductRequest {
    @Schema(example = PRODUCT_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String name;

    @Schema(example = PRODUCT_PRICE_EX, description = MIN_VALUE_DES + PRODUCT_PRICE_MIN)
    @Min(PRODUCT_PRICE_MIN)
    private double price;

    private List<MultipartFile> images;

    @Schema(example = PRODUCT_SIZE_EX, description = OPTIONAL_DES)
    private List<String> size;

    @Schema(example = PRODUCT_DESCRIPTION_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String description;

    @Schema(description = OPTIONAL_DES)
    private List<ToppingModel> toppingList;

    @Schema(example = OBJECT_ID_EX, description = NOT_NULL_DES)
    @NotNull
    private ObjectId categoryId;
}
