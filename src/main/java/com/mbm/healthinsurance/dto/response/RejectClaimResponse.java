package com.mbm.healthinsurance.dto.response;

import com.mbm.healthinsurance.enums.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectClaimResponse {

    private Long claimId;

    private String claimNumber;

    private ClaimStatus status;

    private String remarks;

    private String message;

}