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
public class AdminClaimDetailsResponse {

    // Claim Details
    private Long claimId;
    private String claimNumber;
    private ClaimStatus status;

    // Customer Details
    private Long customerId;
    private String customerName;
    private String email;
    private String mobileNumber;

    // Policy Details
    private Long customerPolicyId;
    private Long policyId;
    private String policyName;

    // Hospital Details
    private String hospitalName;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private String claimReason;

    // Financial Details
    private BigDecimal claimAmount;
    private BigDecimal approvedAmount;

    // Review Details
    private String remarks;

    // Audit
    private LocalDateTime createdAt;
}