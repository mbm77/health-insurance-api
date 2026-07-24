package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.LateFeeType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class CreatePolicyPaymentRuleRequest {


    @NotNull(message = "Grace period is required.")
    @Min(value = 0, message = "Grace period cannot be negative.")
    private Integer gracePeriodInDays;


    @NotNull(message = "Late fee type is required.")
    private LateFeeType lateFeeType;


    @DecimalMin(value = "0.0", message = "Late fee value cannot be negative.")
    private BigDecimal lateFeeValue;


    @NotNull(message = "Allow partial payment is required.")
    private Boolean allowPartialPayment;


    @NotNull(message = "Maximum overdue days is required.")
    @Min(value = 1, message = "Maximum overdue days must be greater than zero.")
    private Integer maxOverdueDays;


    @NotNull(message = "Auto lapse is required.")
    private Boolean autoLapse;

}