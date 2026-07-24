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
public class MyClaimsResponse {

	// Claim details
	private Long claimId;

	private String claimNumber;

	private BigDecimal claimAmount;

	private BigDecimal approvedAmount;

	private ClaimStatus status;

	// Policy details
	private Long customerPolicyId;

	private Long policyId;

	private String policyName;

	// Hospital details
	private String hospitalName;

	private LocalDate admissionDate;

	private LocalDate dischargeDate;

	// Additional information
	private String claimReason;

	private String remarks;

	private LocalDateTime createdAt;
}