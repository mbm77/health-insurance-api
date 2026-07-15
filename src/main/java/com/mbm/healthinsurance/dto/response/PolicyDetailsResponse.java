package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.mbm.healthinsurance.enums.PolicyStatus;

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
public class PolicyDetailsResponse {

    // Company
    private Long companyId;
    private String companyName;

    // Plan
    private Long planId;
    private String planName;
    private String planDescription;
    private BigDecimal coverageAmount;
    private BigDecimal premiumAmount;
    private Integer policyTermInMonths;
    private Integer waitingPeriodInDays;

    // Policy
    private Long policyId;
    private String policyName;
    private String policyCode;
    private String policyType;
    private Integer minAge;
    private Integer maxAge;
    private PolicyStatus status;

    // Coverages
    private List<PolicyCoverageResponse> coverages;

    // Exclusions
    private List<PolicyExclusionResponse> exclusions;
}