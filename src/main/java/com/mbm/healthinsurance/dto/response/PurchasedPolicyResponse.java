package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.PremiumFrequency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasedPolicyResponse {

	// Customer Policy (Purchased Policy)
	private Long customerPolicyId;
	private String policyNumber;

	private LocalDate purchaseDate;
	private LocalDate startDate;
	private LocalDate expiryDate;

	private BigDecimal paidPremiumAmount;
	private PremiumFrequency premiumFrequency;

	private CustomerPolicyStatus customerPolicyStatus;

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

	// Benefits
	private List<PolicyCoverageResponse> coverages;

	// Restrictions
	private List<PolicyExclusionResponse> exclusions;

}