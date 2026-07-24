package com.mbm.healthinsurance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDashboardResponse {

	// Customer
	private Long customerId;
	private String customerName;

	// Policies
	private long totalPolicies;
	private long activePolicies;
	private long expiredPolicies;

	// Claims
	private long totalClaims;
	private long submittedClaims;
	private long approvedClaims;
	private long rejectedClaims;

	// Payments
	private long totalPayments;
	private long successfulPayments;
	private long pendingPayments;
	private long failedPayments;
	private long refundedPayments;

	// Notifications
	private long totalNotifications;
	private long unreadNotifications;

}