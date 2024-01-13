package com.hcmute.drink.dto.request;

import com.hcmute.drink.common.OrderItemDto;
import com.hcmute.drink.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
public class CreateOnsiteOrderRequest {

    @Schema(example = OBJECT_ID_EX)
    @NotNull
    private ObjectId userId;

    @Schema(description = NOT_EMPTY_DES)
    @NotEmpty
    private List<OrderItemDto> itemList;

    @Schema(example = ORDER_NOTE_EX)
    private String note;

    @Schema(example = PAYMENT_TYPE_EX)
    @NotNull
    private PaymentType paymentType;

    @Schema(example = OBJECT_ID_EX)
    @NotNull
    private Date receiveTime;

    @Schema(example = OBJECT_ID_EX)
    @NotNull
    private ObjectId branchId;
}
