package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveClaimResponse {

    private Long claimId;

    private String claimNumber;

    private BigDecimal approvedAmount;

    private ClaimStatus status;

    private String remarks;

    private String message;
}