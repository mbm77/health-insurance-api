package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.CoverageStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePolicyCoverageStatusRequest {

    @NotNull(message = "Coverage status is required")
    private CoverageStatus status;

}