package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PremiumPaymentScheduleStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PremiumPaymentScheduleDetailsResponse {

	/*
	 * Schedule
	 */
	private Long scheduleId;

	private LocalDate dueDate;

	private LocalDate gracePeriodEndDate;

	private BigDecimal amount;

	private PremiumPaymentScheduleStatus status;

	private LocalDate paidDate;

	/*
	 * Customer Policy
	 */
	private Long customerPolicyId;

	private String policyNumber;

	private Long policyId;

	private String policyName;

	/*
	 * Payment
	 */
	private Long paymentId;

	private String paymentNumber;

	/*
	 * Audit
	 */
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}