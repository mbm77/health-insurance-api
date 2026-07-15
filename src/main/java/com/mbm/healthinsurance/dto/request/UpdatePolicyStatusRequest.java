package com.mbm.healthinsurance.dto.request;


import com.mbm.healthinsurance.enums.PolicyStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePolicyStatusRequest {

    @NotNull(message = "Policy status is required")
    private PolicyStatus status;

}