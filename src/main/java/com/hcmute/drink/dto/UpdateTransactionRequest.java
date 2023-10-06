package com.hcmute.drink.dto;

import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Data
@Builder
public class UpdateTransactionRequest {

    @Schema(example = INVOICE_NOTE_EX, description = NOT_BLANK_DES)
    @NotBlank
    private String invoiceCode;

    // xem có cần trường này không
    @Schema(description = OPTIONAL_DES)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Schema(example = TOTAL_PAID_EX, description = NOT_BLANK_DES)
    @NotBlank
    private double totalPaid;


    private String transactionTimeCode;
}
