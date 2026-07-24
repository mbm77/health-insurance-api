package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.LateFeeType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PolicyPaymentRuleRequest {
	@NotNull(message = "Grace period is required.")
	private Integer gracePeriodInDays;

	@NotNull(message = "Late fee type is required.")
	private LateFeeType lateFeeType;

	@NotNull(message = "Penalty value is required.")
	@DecimalMin(value = "0.0", message = "Penalty value must be greater than or equal to zero.")
	private BigDecimal lateFeeValue;

	@NotNull(message = "Allow partial payment is required.")
	private Boolean allowPartialPayment;

	@NotNull(message = "Maximum overdue days is required.")
	@Min(value = 1, message = "Maximum overdue days must be at least 1.")
	private Integer maxOverdueDays;

	@NotNull(message = "Auto lapse is required.")
	private Boolean autoLapse;

}
