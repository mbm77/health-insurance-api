package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.PaymentGateway;
import com.mbm.healthinsurance.enums.PaymentMethod;
import com.mbm.healthinsurance.enums.PaymentType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

    @NotNull(message = "Customer Policy Id is required")
    private Long customerPolicyId;

    @NotNull(message = "Payment Type is required")
    private PaymentType paymentType;

    @NotNull(message = "Payment Method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment Gateway is required")
    private PaymentGateway paymentGateway;

    @Size(max = 500)
    private String remarks;
}