package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.OBJECT_ID_ARRAY_EX;
import static com.hcmute.drink.constant.SwaggerConstant.OBJECT_ID_EX;

@Data
public class DeleteSomeProductRequest {
    @Schema(example = OBJECT_ID_ARRAY_EX)
    @NotNull
    private List<String> productIdList;
}
