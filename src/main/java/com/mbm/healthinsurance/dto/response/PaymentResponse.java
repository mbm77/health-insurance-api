package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PaymentGateway;
import com.mbm.healthinsurance.enums.PaymentMethod;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long paymentId;

    private String paymentNumber;

    private Long customerPolicyId;

    private PaymentType paymentType;

    private PaymentMethod paymentMethod;

    private PaymentGateway paymentGateway;

    private BigDecimal amount;

    private PaymentStatus status;

    private String transactionReference;

    private LocalDateTime paymentDate;

    private String remarks;
}