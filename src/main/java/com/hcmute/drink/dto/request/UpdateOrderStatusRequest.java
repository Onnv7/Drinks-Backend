package com.hcmute.drink.dto.request;

import com.hcmute.drink.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class UpdateOrderStatusRequest {
    @Schema(example = ORDER_STATUS_EX, description = NOT_NULL_DES)
    @NotNull
    private OrderStatus orderStatus;
    @Schema(example = ORDER_STATUS_DES_EX, description = OPTIONAL_DES)
    private String description;
}
