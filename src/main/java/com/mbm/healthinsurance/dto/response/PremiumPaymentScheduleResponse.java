package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mbm.healthinsurance.enums.PremiumPaymentScheduleStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumPaymentScheduleResponse {

    private Long scheduleId;

    private LocalDate dueDate;

    private BigDecimal amount;

    private PremiumPaymentScheduleStatus status;

    private LocalDate paidDate;

    private LocalDate gracePeriodEndDate;

}