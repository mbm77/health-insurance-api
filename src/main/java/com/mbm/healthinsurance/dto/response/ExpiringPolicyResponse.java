package com.mbm.healthinsurance.dto.response;

import java.time.LocalDate;

import com.mbm.healthinsurance.enums.CustomerPolicyStatus;

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
public class ExpiringPolicyResponse {

    private Long customerPolicyId;

    private String policyNumber;

    private String policyName;

    private LocalDate expiryDate;

    private long daysRemaining;

    private CustomerPolicyStatus status;

}