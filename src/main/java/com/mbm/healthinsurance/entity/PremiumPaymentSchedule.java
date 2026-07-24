package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mbm.healthinsurance.enums.PremiumPaymentScheduleStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "premium_payment_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumPaymentSchedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_policy_id", nullable = false)
    private CustomerPolicy customerPolicy;


    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;


    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PremiumPaymentScheduleStatus status =
            PremiumPaymentScheduleStatus.PENDING;


    @Column(name = "paid_date")
    private LocalDate paidDate;


    @Column(name = "payment_id")
    private Long paymentId;


    @Column(name = "grace_period_end_date")
    private LocalDate gracePeriodEndDate;

}