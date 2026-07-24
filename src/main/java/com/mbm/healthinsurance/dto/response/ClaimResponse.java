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
public class ClaimResponse {


    private Long claimId;

    private String claimNumber;

    private BigDecimal claimAmount;

    private ClaimStatus status;

    private String message;

}