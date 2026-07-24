package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.PremiumFrequency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerPolicyResponse {

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