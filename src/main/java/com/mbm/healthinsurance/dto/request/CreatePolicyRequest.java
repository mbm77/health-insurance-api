package com.mbm.healthinsurance.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePolicyRequest {

    @NotNull(message = "Plan Id is required")
    private Long planId;

    @NotBlank(message = "Policy name is required")
    private String policyName;

    @NotBlank(message = "Policy code is required")
    private String policyCode;

    @NotBlank(message = "Policy type is required")
    private String policyType;

    @NotNull(message = "Minimum age is required")
    @Min(value = 0, message = "Minimum age cannot be negative")
    private Integer minAge;

    @NotNull(message = "Maximum age is required")
    @Min(value = 0, message = "Maximum age cannot be negative")
    private Integer maxAge;
}