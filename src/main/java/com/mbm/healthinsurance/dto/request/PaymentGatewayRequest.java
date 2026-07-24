package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.PaymentMethod;

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
public class PaymentGatewayRequest {

    private String paymentNumber;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;
}