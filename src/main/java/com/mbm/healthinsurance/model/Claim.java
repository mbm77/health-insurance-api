package com.mbm.healthinsurance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;

    private String claimNumber;

    @ManyToOne
    @JoinColumn(name = "customer_policy_id")
    private CustomerPolicy customerPolicy;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private String claimType;

    private LocalDate admissionDate;

    private LocalDate dischargeDate;

    private BigDecimal claimedAmount;

    private BigDecimal approvedAmount;

    private String diagnosis;

    private String claimReason;

    private String status;

    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;
}