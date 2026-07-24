package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mbm.healthinsurance.enums.ClaimStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "claim")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Long claimId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_policy_id", nullable = false)
    private CustomerPolicy customerPolicy;

    @NotBlank(message = "Claim number is required.")
    @Column(name = "claim_number", nullable = false, unique = true, length = 30)
    private String claimNumber;

    @NotBlank(message = "Hospital name is required.")
    @Column(name = "hospital_name", nullable = false, length = 150)
    private String hospitalName;

    @NotNull(message = "Admission date is required.")
    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @NotNull(message = "Discharge date is required.")
    @Column(name = "discharge_date", nullable = false)
    private LocalDate dischargeDate;

    @NotNull(message = "Claim amount is required.")
    @DecimalMin(value = "1.00", message = "Claim amount must be greater than zero.")
    @Column(name = "claim_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal claimAmount;

    @Column(name = "approved_amount", precision = 12, scale = 2)
    private BigDecimal approvedAmount;

    @NotBlank(message = "Claim reason is required.")
    @Column(name = "claim_reason", nullable = false, length = 500)
    private String claimReason;

    @NotNull(message = "Claim status is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClaimStatus status;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}