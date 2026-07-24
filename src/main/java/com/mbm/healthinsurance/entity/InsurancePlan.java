package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.PlanStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "insurance_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "plan_name", nullable = false, unique = true, length = 100)
    private String planName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "coverage_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "premium_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal premiumAmount;

    @Column(name = "policy_term_in_months", nullable = false)
    private Integer policyTermInMonths;

    @Column(name = "waiting_period_in_days", nullable = false)
    private Integer waitingPeriodInDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PlanStatus status = PlanStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private InsuranceCompany insuranceCompany;
}