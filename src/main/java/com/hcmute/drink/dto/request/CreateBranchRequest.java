package com.hcmute.drink.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateBranchRequest {

    @Schema(example = PROVINCE_EX)
    @NotBlank
    private String province;

    @Schema(example = COMMUNE_EX)
    @NotBlank
    private String commune;

    @Schema(example = DISTRICT_EX)
    @NotBlank
    private String district;

    @Schema(example = ADDRESS_DETAILS_EX)
    @NotBlank
    private String details;
}
