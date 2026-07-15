package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.CompanyStatus;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompanyStatusRequest {

    @NotNull(message = "Status is required")
    private CompanyStatus status;
}