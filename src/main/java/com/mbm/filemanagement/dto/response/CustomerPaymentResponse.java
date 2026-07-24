package com.mbm.filemanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PaymentMethod;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;

import lombok.Data;

@Data
public class CustomerPaymentResponse {

    private Long paymentId;
    private String paymentNumber;

    private PaymentType paymentType;
    private PaymentMethod paymentMethod;

    private BigDecimal amount;
    private PaymentStatus status;

    private String transactionReference;
    private LocalDateTime paymentDate;
    private String remarks;

    private Long customerPolicyId;
    private String policyNumber;
    private String policyName;

    private Long customerId;
    private String customerName;
}