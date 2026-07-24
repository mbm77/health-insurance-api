package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.LateFeeType;
import com.mbm.healthinsurance.enums.PremiumFrequency;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_policy_id")
	private Long customerPolicyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id", nullable = false)
	private Policy policy;

	@Column(name = "policy_number", nullable = false, unique = true, length = 100)
	private String policyNumber;

	@Column(name = "purchase_date", nullable = false)
	private LocalDate purchaseDate;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate expiryDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "premium_frequency", nullable = false, length = 20)
	private PremiumFrequency premiumFrequency;

	@Column(name = "coverage_amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal coverageAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private CustomerPolicyStatus status;

	@Column(name = "premium_amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal premiumAmount;

	@Column(name = "grace_period_in_days")
	private Integer gracePeriodInDays;

	@Enumerated(EnumType.STRING)
	@Column(name = "late_fee_type", length = 20)
	private LateFeeType lateFeeType;

	@Column(name = "late_fee_value", precision = 12, scale = 2)
	private BigDecimal lateFeeValue;

	@Column(name = "allow_partial_payment")
	private Boolean allowPartialPayment;

	@Column(name = "max_overdue_days")
	private Integer maxOverdueDays;

	@Column(name = "auto_lapse")
	private Boolean autoLapse;

	@OneToMany(mappedBy = "customerPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PremiumPaymentSchedule> paymentSchedules = new ArrayList<>();
}