package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.LateFeeType;

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
public class PolicyPaymentRuleResponse {


    private Long ruleId;


    private Long policyId;

    private String policyName;


    private Integer gracePeriodInDays;


    private LateFeeType lateFeeType;


    private BigDecimal lateFeeValue;


    private Boolean allowPartialPayment;


    private Integer maxOverdueDays;


    private Boolean autoLapse;

}