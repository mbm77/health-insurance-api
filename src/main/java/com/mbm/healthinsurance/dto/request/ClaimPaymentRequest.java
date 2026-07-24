package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.PaymentGateway;
import com.mbm.healthinsurance.enums.PaymentMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimPaymentRequest {

    @NotNull(message = "Payment method is required.")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment gateway is required.")
    private PaymentGateway paymentGateway;

    @NotBlank(message = "Transaction reference is required.")
    private String transactionReference;

    @Size(max = 500)
    private String remarks;
}