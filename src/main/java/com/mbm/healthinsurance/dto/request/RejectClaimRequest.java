package com.mbm.healthinsurance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RejectClaimRequest {

    @NotBlank(message = "Remarks are required.")
    @Size(max = 500,
            message = "Remarks cannot exceed 500 characters.")
    private String remarks;

}