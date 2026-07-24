package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PaymentMethod;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;

import lombok.Data;

@Data
public class PremiumHistoryResponse {

    private Long paymentId;

    private String paymentNumber;

    private Long customerPolicyId;

    private String policyName;

    private PaymentType paymentType;

    private PaymentMethod paymentMethod;

    private BigDecimal amount;

    private PaymentStatus status;

    private String transactionReference;

    private LocalDateTime paymentDate;

    private String remarks;
}