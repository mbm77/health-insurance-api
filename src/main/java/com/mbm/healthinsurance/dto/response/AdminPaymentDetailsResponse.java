package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PaymentMethod;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminPaymentDetailsResponse {

    /*
     * Payment
     */
    private Long paymentId;

    private String paymentNumber;

    private PaymentType paymentType;

    private PaymentMethod paymentMethod;

    private BigDecimal amount;

    private PaymentStatus status;

    private String transactionReference;

    private LocalDateTime paymentDate;

    private String remarks;

    /*
     * Customer
     */
    private Long customerId;

    private String customerName;

    private String email;

    private String mobileNumber;

    /*
     * Policy
     */
    private Long customerPolicyId;

    private String policyNumber;

    private Long policyId;

    private String policyName;

    /*
     * Future Claim Payment Support
     */
    private Long claimId;

    private String claimNumber;

    /*
     * Audit
     */
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    private String paymentGateway;

}