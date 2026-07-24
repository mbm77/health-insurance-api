package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mbm.healthinsurance.enums.RenewalStatus;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "policy_renewal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyRenewal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "renewal_id")
	private Long renewalId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_policy_id", nullable = false)
	private CustomerPolicy customerPolicy;

	@NotNull(message = "Renewal number is required.")
	@Column(name = "renewal_number", nullable = false)
	private Integer renewalNumber;

	@NotNull(message = "Renewal date is required.")
	@Column(name = "renewal_date", nullable = false)
	private LocalDate renewalDate;

	@NotNull(message = "Old start date is required.")
	@Column(name = "old_start_date", nullable = false)
	private LocalDate oldStartDate;

	@NotNull(message = "Old expiry date is required.")
	@Column(name = "old_expiry_date", nullable = false)
	private LocalDate oldExpiryDate;

	@NotNull(message = "New start date is required.")
	@Column(name = "new_start_date", nullable = false)
	private LocalDate newStartDate;

	@NotNull(message = "New expiry date is required.")
	@Column(name = "new_expiry_date", nullable = false)
	private LocalDate newExpiryDate;

	@NotNull(message = "Premium amount is required.")
	@Positive(message = "Premium amount must be greater than zero.")
	@Column(name = "premium_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal premiumAmount;

	@NotNull(message = "Renewal status is required.")
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private RenewalStatus status;

	@Column(name = "remarks", length = 500)
	private String remarks;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
