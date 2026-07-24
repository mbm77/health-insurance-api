package com.mbm.healthinsurance.dto.request;


import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.PaymentMethod;
import com.mbm.healthinsurance.enums.PaymentType;

import lombok.Data;


@Data
public class PaymentRequest {

    private Long customerPolicyId;

    private Long claimId; // optional for claim settlement

    private PaymentType paymentType;

    private PaymentMethod paymentMethod;

    private BigDecimal amount;

    private String remarks;
}
