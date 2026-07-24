package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.AppealCategory;
import com.mbm.healthinsurance.enums.ReferenceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAppealRequest {

    @NotNull(message = "Appeal category is required.")
    private AppealCategory category;

    @NotBlank(message = "Subject is required.")
    private String subject;

    @NotBlank(message = "Description is required.")
    private String description;

    private ReferenceType referenceType;

    private Long referenceId;
}