package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.PaymentGateway;
import com.mbm.healthinsurance.enums.PaymentMethod;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@Column(unique = true, nullable = false)
	private String paymentNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_policy_id", nullable = false)
	private CustomerPolicy customerPolicy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claim_id")
	private Claim claim;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentType paymentType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMethod paymentMethod;

	@Column(nullable = false)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	private String transactionReference;

	private LocalDateTime paymentDate;

	@Column(name = "remarks", length = 500)
	private String remarks;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {

		createdAt = LocalDateTime.now();

		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {

		updatedAt = LocalDateTime.now();
	}

	@Column(name = "refund_reference", length = 100)
	private String refundReference;

	@Column(name = "refund_date")
	private LocalDateTime refundDate;

	@Column(name = "refund_remarks", length = 500)
	private String refundRemarks;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_gateway", length = 50)
	private PaymentGateway paymentGateway;

	@Column(name = "gateway_payment_id", length = 100)
	private String gatewayPaymentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "premium_schedule_id")
	private PremiumPaymentSchedule premiumPaymentSchedule;

}