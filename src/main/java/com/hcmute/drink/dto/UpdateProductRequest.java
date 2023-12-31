package com.hcmute.drink.dto;

import com.hcmute.drink.common.SizeModel;
import com.hcmute.drink.common.ToppingModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateProductRequest implements Serializable {

    @Schema(example = PRODUCT_NAME_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String name;

    @Schema( description = OPTIONAL_DES)
    private MultipartFile image;


    // example = PRODUCT_SIZE_EX,
    @Schema(description = NOT_EMPTY_DES)
    @NotEmpty
    private List<SizeModel> sizeList;

    @Schema(example = PRODUCT_DESCRIPTION_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String description;

    @Schema(description = OPTIONAL_DES)
    private List<ToppingModel> toppingList;

    @Schema(example = OBJECT_ID_EX, description = NOT_NULL_DES)
    @NotNull
    private ObjectId categoryId;

    @Schema(example = BOOLEAN_EX, description = NOT_NULL_DES)
    @NotNull
    private boolean enabled;
}
