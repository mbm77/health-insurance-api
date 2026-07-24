package com.mbm.healthinsurance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResolveAppealRequest {

    @NotBlank(message = "Admin remarks are required.")
    @Size(max = 1000, message = "Admin remarks cannot exceed 1000 characters.")
    private String adminRemarks;
}