package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApproveClaimRequest {

    @NotNull(message = "Approved amount is required.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Approved amount must be greater than zero.")
    private BigDecimal approvedAmount;

    @Size(max = 500,
            message = "Remarks cannot exceed 500 characters.")
    private String remarks;
}