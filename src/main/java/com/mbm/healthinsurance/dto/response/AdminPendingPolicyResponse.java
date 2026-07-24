package com.mbm.healthinsurance.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminPendingPolicyResponse {

    private Long customerPolicyId;

    private Long customerId;

    private String customerName;

    private String policyNumber;

    private String policyName;

    private BigDecimal premiumAmount;

    private String status;

    private LocalDate startDate;
}