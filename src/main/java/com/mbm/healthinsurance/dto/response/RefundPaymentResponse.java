package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundPaymentResponse {

    private Long paymentId;

    private String paymentNumber;

    private PaymentStatus status;

    private String refundReference;

    private LocalDateTime refundDate;

    private String refundRemarks;

    private String message;

}