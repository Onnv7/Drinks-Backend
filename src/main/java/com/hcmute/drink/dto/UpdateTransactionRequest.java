package com.hcmute.drink.dto;

import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class UpdateTransactionRequest {

    @NotBlank
    private String invoiceCode;
    @Builder.Default
    private PaymentStatus status = PaymentStatus.UNPAID;

    @NotBlank
    private double totalPaid;


    private String transactionTimeCode;
}
