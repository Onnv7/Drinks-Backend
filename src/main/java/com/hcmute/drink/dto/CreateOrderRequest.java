package com.hcmute.drink.dto;

import com.hcmute.drink.common.AddressModel;
import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

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

    @Schema(example = ORDER_NOTE_EX, description = OPTIONAL_DES)
    private String note;

    @Schema(example = PAYMENT_STATUS_EX, description = NOT_BLANK_DES)
    @NotNull
    private PaymentStatus paymentStatus;

    @Schema(example = PAYMENT_TYPE_EX, description = NOT_BLANK_DES)
    @NotNull
    private PaymentType paymentType;

    @Schema(description = NOT_NULL_DES)
    @NotNull
    private AddressModel address;
}
