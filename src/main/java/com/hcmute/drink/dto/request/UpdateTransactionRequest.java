package com.hcmute.drink.dto.request;

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

    @Schema(example = INVOICE_NOTE_EX)
    @NotBlank
    private String invoiceCode;

    // xem có cần trường này không
    @Schema()
    @Builder.Default
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Schema(example = TOTAL_PAID_EX)
    @NotNull
    private double totalPaid;


    private String transactionTimeCode;
}
