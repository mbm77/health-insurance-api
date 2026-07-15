package com.mbm.healthinsurance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePolicyExclusionRequest {

    @NotNull(message = "Policy Id is required")
    private Long policyId;

    @NotBlank(message = "Exclusion name is required")
    private String exclusionName;

    private String description;

}