package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePolicyCoverageRequest {

    @NotNull(message = "Policy Id is required")
    private Long policyId;

    @NotBlank(message = "Coverage name is required")
    private String coverageName;

    @NotNull(message = "Coverage amount is required")
    @DecimalMin(value = "0.01", message = "Coverage amount must be greater than zero")
    private BigDecimal coverageAmount;

    private String description;
}