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
public class UpdateInsurancePlanRequest {

    @NotBlank(message = "Plan name is required")
    private String planName;

    private String description;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal coverageAmount;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal premiumAmount;

    @NotNull
    @Positive
    private Integer policyTermInMonths;

    @NotNull
    private Integer waitingPeriodInDays;
}