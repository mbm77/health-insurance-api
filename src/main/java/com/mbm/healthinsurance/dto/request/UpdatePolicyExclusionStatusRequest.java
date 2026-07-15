package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.ExclusionStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePolicyExclusionStatusRequest {

    @NotNull(message = "Exclusion status is required")
    private ExclusionStatus status;

}