package com.mbm.healthinsurance.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAdminCustomerStatusRequest {

    @NotNull(message = "Enabled status is required")
    private Boolean enabled;
}