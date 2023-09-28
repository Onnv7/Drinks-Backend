package com.hcmute.drink.dto;

import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
public class UpdateTransactionRequest {
    @NotNull
    private ObjectId userId;
    @NotEmpty
    private List<OrderDetailsModel> products;

    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
    private String note;
}
