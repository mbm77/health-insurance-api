package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentPaymentResponse {

    private Long paymentId;

    private String paymentNumber;

    private String policyName;

    private PaymentType paymentType;

    private BigDecimal amount;

    private PaymentStatus status;

    private LocalDateTime paymentDate;

}