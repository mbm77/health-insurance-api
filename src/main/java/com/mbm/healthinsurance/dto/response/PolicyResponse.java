package com.mbm.healthinsurance.dto.response;

import com.mbm.healthinsurance.enums.PolicyStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PolicyResponse {

    private Long policyId;

    private Long companyId;

    private String companyName;

    private Long planId;

    private String planName;

    private String policyName;

    private String policyCode;

    private String policyType;

    private Integer minAge;

    private Integer maxAge;

    private PolicyStatus status;
}