package com.hcmute.drink.dto;

import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.common.OrderLogModel;
import com.hcmute.drink.common.ReviewModel;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateOrderRequest {
    @Schema(example = OBJECT_ID_EX, description = NOT_NULL_DES)
    @NotNull
    private ObjectId userId;

    @Schema(description = NOT_EMPTY_DES)
    @NotEmpty
    private List<OrderDetailsModel> products;

    private String note;

    @NotBlank
    private OrderType orderType;

    @NotBlank
    private PaymentStatus paymentStatus;

    @NotBlank
    private PaymentType paymentType;
}
