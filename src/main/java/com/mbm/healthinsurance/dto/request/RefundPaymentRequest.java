package com.mbm.healthinsurance.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class RefundPaymentRequest {

    @NotBlank(message = "Refund remarks are required")
    @Size(max = 500, message = "Refund remarks cannot exceed 500 characters")
    private String refundRemarks;
}