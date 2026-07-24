package com.mbm.healthinsurance.dto.response;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminClaimsResponse {

    // Claim Details
    private Long claimId;
    private String claimNumber;
    private ClaimStatus status;

    // Customer Details
    private Long customerId;
    private String customerName;

    // Policy Details
    private Long customerPolicyId;
    private Long policyId;
    private String policyName;

    // Hospital Details
    private String hospitalName;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;

    // Financial Details
    private BigDecimal claimAmount;
    private BigDecimal approvedAmount;

    // Audit
    private LocalDateTime createdAt;
}