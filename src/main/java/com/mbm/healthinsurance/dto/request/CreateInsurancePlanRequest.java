package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInsurancePlanRequest {

    @NotNull(message = "Company Id is required")
    private Long companyId;

    @NotBlank(message = "Plan name is required")
    private String planName;

    private String description;

    @NotNull(message = "Coverage amount is required")
    @DecimalMin(value = "0.01", message = "Coverage amount must be greater than zero")
    private BigDecimal coverageAmount;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "0.01", message = "Premium amount must be greater than zero")
    private BigDecimal premiumAmount;

    @NotNull(message = "Policy term is required")
    @Positive(message = "Policy term must be greater than zero")
    private Integer policyTermInMonths;

    @NotNull(message = "Waiting period is required")
    private Integer waitingPeriodInDays;
}