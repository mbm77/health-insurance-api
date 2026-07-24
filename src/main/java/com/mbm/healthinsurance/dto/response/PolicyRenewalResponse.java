package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mbm.healthinsurance.enums.RenewalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRenewalResponse {

    private Long renewalId;

    private Long customerPolicyId;

    private Integer renewalNumber;

    private LocalDate renewalDate;

    private LocalDate newStartDate;

    private LocalDate newExpiryDate;

    private BigDecimal premiumAmount;

    private RenewalStatus status;

    private String message;

}