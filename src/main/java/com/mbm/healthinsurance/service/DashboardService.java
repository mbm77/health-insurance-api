package com.mbm.healthinsurance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.response.AdminDashboardResponse;
import com.mbm.healthinsurance.dto.response.CustomerDashboardResponse;
import com.mbm.healthinsurance.dto.response.ExpiringPolicyResponse;
import com.mbm.healthinsurance.dto.response.RecentClaimResponse;
import com.mbm.healthinsurance.dto.response.RecentNotificationResponse;
import com.mbm.healthinsurance.dto.response.RecentPaymentResponse;
import com.mbm.healthinsurance.entity.Claim;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.Notification;
import com.mbm.healthinsurance.entity.Payment;
import com.mbm.healthinsurance.enums.ClaimStatus;
import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.NotificationStatus;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PolicyStatus;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.ClaimRepository;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.InsuranceCompanyRepository;
import com.mbm.healthinsurance.repository.InsurancePlanRepository;
import com.mbm.healthinsurance.repository.NotificationRepository;
import com.mbm.healthinsurance.repository.PaymentRepository;
import com.mbm.healthinsurance.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final CustomerRepository customerRepository;

	private final CustomerPolicyRepository customerPolicyRepository;

	private final ClaimRepository claimRepository;

	private final PaymentRepository paymentRepository;

	private final NotificationRepository notificationRepository;
	private final InsuranceCompanyRepository insuranceCompanyRepository;
	private final InsurancePlanRepository insurancePlanRepository;
	private final PolicyRepository policyRepository;

	public CustomerDashboardResponse getCustomerDashboard(
			Authentication authentication) {

		Customer customer = customerRepository
				.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found."));

		return CustomerDashboardResponse.builder()

				// Customer
				.customerId(customer.getCustomerId())
				.customerName(
						customer.getFirstName() + " "
								+ customer.getLastName())

				// Policies
				.totalPolicies(
						customerPolicyRepository.countByCustomer(customer))

				.activePolicies(
						customerPolicyRepository.countByCustomerAndStatus(
								customer,
								CustomerPolicyStatus.ACTIVE))

				.expiredPolicies(
						customerPolicyRepository.countByCustomerAndStatus(
								customer,
								CustomerPolicyStatus.EXPIRED))

				// Claims
				.totalClaims(
						claimRepository.countByCustomerPolicyCustomer(
								customer))

				.submittedClaims(
						claimRepository
								.countByCustomerPolicyCustomerAndStatus(
										customer,
										ClaimStatus.SUBMITTED))

				.approvedClaims(
						claimRepository
								.countByCustomerPolicyCustomerAndStatus(
										customer,
										ClaimStatus.APPROVED))

				.rejectedClaims(
						claimRepository
								.countByCustomerPolicyCustomerAndStatus(
										customer,
										ClaimStatus.REJECTED))

				// Payments
				.totalPayments(
						paymentRepository.countByCustomer(customer))

				.successfulPayments(
						paymentRepository.countByCustomerAndStatus(
								customer,
								PaymentStatus.SUCCESS))

				.pendingPayments(
						paymentRepository.countByCustomerAndStatus(
								customer,
								PaymentStatus.PENDING))

				.failedPayments(
						paymentRepository.countByCustomerAndStatus(
								customer,
								PaymentStatus.FAILED))

				.refundedPayments(
						paymentRepository.countByCustomerAndStatus(
								customer,
								PaymentStatus.REFUNDED))

				// Notifications
				.totalNotifications(
						notificationRepository.countByCustomer(
								customer))

				.unreadNotifications(
						notificationRepository
								.countByCustomerAndStatus(
										customer,
										NotificationStatus.UNREAD))

				.build();
	}

	public List<RecentClaimResponse> getRecentClaims(
			Authentication authentication) {

		Customer customer = customerRepository
				.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found."));

		List<Claim> claims = claimRepository
				.findTop5ByCustomerPolicyCustomerOrderByCreatedAtDesc(
						customer);

		return claims.stream()
				.map(this::mapToRecentClaimResponse)
				.toList();
	}

	private RecentClaimResponse mapToRecentClaimResponse(
			Claim claim) {

		return RecentClaimResponse.builder()

				.claimId(claim.getClaimId())

				.claimNumber(claim.getClaimNumber())

				.policyName(
						claim.getCustomerPolicy()
								.getPolicy()
								.getPolicyName())

				.claimAmount(claim.getClaimAmount())

				.status(claim.getStatus())

				.createdAt(claim.getCreatedAt())

				.build();
	}

	public List<RecentPaymentResponse> getRecentPayments(
			Authentication authentication) {

		Customer customer = customerRepository
				.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found."));

		List<Payment> payments = paymentRepository
				.findTop5ByCustomerOrderByCreatedAtDesc(
						customer);

		return payments.stream()
				.map(this::mapToRecentPaymentResponse)
				.toList();
	}

	private RecentPaymentResponse mapToRecentPaymentResponse(
			Payment payment) {

		return RecentPaymentResponse.builder()

				.paymentId(
						payment.getPaymentId())

				.paymentNumber(
						payment.getPaymentNumber())

				.policyName(
						payment.getCustomerPolicy()
								.getPolicy()
								.getPolicyName())

				.paymentType(
						payment.getPaymentType())

				.amount(
						payment.getAmount())

				.status(
						payment.getStatus())

				.paymentDate(
						payment.getPaymentDate())

				.build();
	}

	public List<RecentNotificationResponse> getRecentNotifications(
			Authentication authentication) {

		Customer customer = customerRepository
				.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found."));

		List<Notification> notifications = notificationRepository
				.findTop5ByCustomerOrderByCreatedAtDesc(
						customer);

		return notifications.stream()
				.map(this::mapToRecentNotificationResponse)
				.toList();
	}

	private RecentNotificationResponse mapToRecentNotificationResponse(
			Notification notification) {

		return RecentNotificationResponse.builder()

				.notificationId(
						notification.getNotificationId())

				.title(
						notification.getTitle())

				.message(
						notification.getMessage())

				.notificationType(
						notification.getNotificationType())

				.status(
						notification.getStatus())

				.createdAt(
						notification.getCreatedAt())

				.build();
	}

	public List<ExpiringPolicyResponse> getExpiringPolicies(
			Authentication authentication) {

		Customer customer = customerRepository
				.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found."));

		LocalDate today = LocalDate.now();

		LocalDate expiryLimit = today.plusDays(30);

		List<CustomerPolicy> policies = customerPolicyRepository
				.findByCustomerAndExpiryDateBetween(
						customer,
						today,
						expiryLimit);

		return policies.stream()
				.map(this::mapToExpiringPolicyResponse)
				.toList();
	}

	private ExpiringPolicyResponse mapToExpiringPolicyResponse(
			CustomerPolicy customerPolicy) {

		LocalDate today = LocalDate.now();

		long daysRemaining = today.until(
				customerPolicy.getExpiryDate())
				.getDays();

		return ExpiringPolicyResponse.builder()

				.customerPolicyId(
						customerPolicy.getCustomerPolicyId())

				.policyNumber(
						customerPolicy.getPolicyNumber())

				.policyName(
						customerPolicy.getPolicy()
								.getPolicyName())

				.expiryDate(
						customerPolicy.getExpiryDate())

				.daysRemaining(
						daysRemaining)

				.status(
						customerPolicy.getStatus())

				.build();
	}

	public AdminDashboardResponse getAdminDashboard() {

		return AdminDashboardResponse.builder()

				// Customers

				.totalCustomers(
						customerRepository.count())

				.activeCustomers(
						customerRepository.countByUserEnabled(true))

				.inactiveCustomers(
						customerRepository.countByUserEnabled(false))

				// Companies

				.totalCompanies(
						insuranceCompanyRepository.count())

				// Plans

				.totalPlans(
						insurancePlanRepository.count())

				// Policies

				.totalPolicies(
						policyRepository.count())

				.activePolicies(
						policyRepository.countByStatus(
								PolicyStatus.ACTIVE))

				.inactivePolicies(
						policyRepository.countByStatus(
								PolicyStatus.INACTIVE))

				// Customer Policies

				.totalCustomerPolicies(
						customerPolicyRepository.count())

				.activeCustomerPolicies(
						customerPolicyRepository.countByStatus(
								CustomerPolicyStatus.ACTIVE))

				.expiredCustomerPolicies(
						customerPolicyRepository.countByStatus(
								CustomerPolicyStatus.EXPIRED))

				// Claims

				.totalClaims(
						claimRepository.count())

				.submittedClaims(
						claimRepository.countByStatus(
								ClaimStatus.SUBMITTED))

				.approvedClaims(
						claimRepository.countByStatus(
								ClaimStatus.APPROVED))

				.rejectedClaims(
						claimRepository.countByStatus(
								ClaimStatus.REJECTED))

				// Payments

				.totalPayments(
						paymentRepository.count())

				.successfulPayments(
						paymentRepository
								.countByStatus(
										PaymentStatus.SUCCESS))

				.pendingPayments(
						paymentRepository.countByStatus(
								PaymentStatus.PENDING))

				.failedPayments(
						paymentRepository.countByStatus(
								PaymentStatus.FAILED))

				.refundedPayments(
						paymentRepository.countByStatus(
								PaymentStatus.REFUNDED))

				.build();
	}

	
}