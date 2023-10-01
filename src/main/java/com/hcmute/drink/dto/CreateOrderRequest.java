package com.hcmute.drink.dto;

import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.common.OrderLogModel;
import com.hcmute.drink.common.ReviewModel;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentType;
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

@Data
public class CreateOrderRequest {
    @NotNull
    private ObjectId userId;

    @NotEmpty
    private List<OrderDetailsModel> products;

    private String note;

    @NotBlank
    private OrderType orderType;

    @NotBlank
    private PaymentType paymentType;
}
