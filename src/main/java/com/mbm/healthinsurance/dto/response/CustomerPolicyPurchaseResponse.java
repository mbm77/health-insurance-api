package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.PremiumFrequency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPolicyPurchaseResponse {

	private Long customerPolicyId;
	private String policyNumber;

	private String companyName;
	private String planName;
	private String policyName;

	private LocalDate purchaseDate;
	private LocalDate startDate;
	private LocalDate expiryDate;

	private PremiumFrequency premiumFrequency;
	private BigDecimal premiumAmount;
	private BigDecimal coverageAmount;

	private CustomerPolicyStatus status;

}
