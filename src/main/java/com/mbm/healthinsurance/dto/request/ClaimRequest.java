package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequest {


    @NotBlank(message = "Hospital name is required.")
    private String hospitalName;


    @NotNull(message = "Admission date is required.")
    private LocalDate admissionDate;


    @NotNull(message = "Discharge date is required.")
    private LocalDate dischargeDate;


    @NotNull(message = "Claim amount is required.")
    @DecimalMin(
            value = "1.00",
            message = "Claim amount must be greater than zero."
    )
    private BigDecimal claimAmount;


    @NotBlank(message = "Claim reason is required.")
    private String claimReason;

}