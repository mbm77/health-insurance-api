package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentClaimResponse {

    private Long claimId;

    private String claimNumber;

    private String policyName;

    private BigDecimal claimAmount;

    private ClaimStatus status;

    private LocalDateTime createdAt;

}