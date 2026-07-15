package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.PlanStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInsurancePlanStatusRequest {

    @NotNull(message = "Status is required")
    private PlanStatus status;
}