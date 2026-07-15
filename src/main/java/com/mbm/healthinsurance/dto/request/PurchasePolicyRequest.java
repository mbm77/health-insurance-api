package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.PremiumFrequency;

import jakarta.validation.constraints.NotNull;
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
public class PurchasePolicyRequest {

    @NotNull(message = "Policy Id is required")
    private Long policyId;
    
    @NotNull(message="Premium frequency is required")
    private PremiumFrequency premiumFrequency;
}