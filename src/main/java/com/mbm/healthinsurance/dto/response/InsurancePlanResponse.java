package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.PlanStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsurancePlanResponse {

    private Long planId;

    private Long companyId;

    private String companyName;

    private String planName;

    private String description;

    private BigDecimal coverageAmount;

    private BigDecimal premiumAmount;

    private Integer policyTermInMonths;

    private Integer waitingPeriodInDays;

    private PlanStatus status;
}