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
public class PolicyRenewalHistoryResponse {

    private Long renewalId;

    private Integer renewalNumber;

    private LocalDate renewalDate;

    private LocalDate oldStartDate;

    private LocalDate oldExpiryDate;

    private LocalDate newStartDate;

    private LocalDate newExpiryDate;

    private BigDecimal premiumAmount;

    private RenewalStatus status;
}