package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.LateFeeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "policy_payment_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyPaymentRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false, unique = true)
    private Policy policy;


    @Column(name = "grace_period_in_days", nullable = false)
    private Integer gracePeriodInDays;


    @Enumerated(EnumType.STRING)
    @Column(name = "late_fee_type", nullable = false, length = 20)
    private LateFeeType lateFeeType;


    @Column(name = "late_fee_value", precision = 12, scale = 2)
    private BigDecimal lateFeeValue;


    @Column(name = "allow_partial_payment", nullable = false)
    @Builder.Default
    private Boolean allowPartialPayment = false;


    @Column(name = "max_overdue_days", nullable = false)
    @Builder.Default
    private Integer maxOverdueDays = 60;


    @Column(name = "auto_lapse", nullable = false)
    @Builder.Default
    private Boolean autoLapse = true;
}