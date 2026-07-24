package com.mbm.healthinsurance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.filemanagement.dto.response.CustomerPaymentResponse;
import com.mbm.healthinsurance.dto.request.ClaimPaymentRequest;
import com.mbm.healthinsurance.dto.request.PaymentRequest;
import com.mbm.healthinsurance.dto.request.RefundPaymentRequest;
import com.mbm.healthinsurance.dto.response.AdminPaymentDetailsResponse;
import com.mbm.healthinsurance.dto.response.AdminPaymentResponse;
import com.mbm.healthinsurance.dto.response.ClaimPaymentResponse;
import com.mbm.healthinsurance.dto.response.PaymentDetailsResponse;
import com.mbm.healthinsurance.dto.response.PaymentHistoryResponse;
import com.mbm.healthinsurance.dto.response.PaymentResponse;
import com.mbm.healthinsurance.dto.response.PremiumHistoryResponse;
import com.mbm.healthinsurance.dto.response.RefundPaymentResponse;
import com.mbm.healthinsurance.entity.Claim;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.Notification;
import com.mbm.healthinsurance.entity.Payment;
import com.mbm.healthinsurance.entity.User;
import com.mbm.healthinsurance.enums.ClaimStatus;
import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.NotificationStatus;
import com.mbm.healthinsurance.enums.NotificationType;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;
import com.mbm.healthinsurance.enums.ReferenceType;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.ClaimRepository;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.NotificationRepository;
import com.mbm.healthinsurance.repository.PaymentRepository;
import com.mbm.healthinsurance.repository.UserRepository;
import com.mbm.healthinsurance.util.PaymentReferenceGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final CustomerRepository customerRepository;

	private final CustomerPolicyRepository customerPolicyRepository;

	private final PaymentRepository paymentRepository;

	private final PaymentReferenceGenerator paymentReferenceGenerator;

	private final NotificationService notificationService;

	private final PremiumPaymentScheduleService premiumPaymentScheduleService;
	private final ClaimRepository claimRepository;
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	@Transactional
	public PaymentResponse createPayment(
			Authentication authentication,
			PaymentRequest request) {

		String username = authentication.getName();

		// 1. Get logged-in customer

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(
						() -> new RuntimeException(
								"Customer not found"));

		// 2. Find customer policy

		CustomerPolicy customerPolicy = customerPolicyRepository
				.findById(request.getCustomerPolicyId())
				.orElseThrow(
						() -> new RuntimeException(
								"Customer policy not found"));

		// 3. Validate ownership

		if (!customerPolicy.getCustomer()
				.getCustomerId()
				.equals(customer.getCustomerId())) {

			throw new BadRequestException(
					"Policy does not belong to customer.");
		}

		boolean paymentExists = paymentRepository
				.existsByCustomerPolicyAndPaymentTypeAndStatus(
						customerPolicy,
						request.getPaymentType(),
						PaymentStatus.PENDING);

		if (paymentExists) {

			throw new BadRequestException(
					"Pending payment already exists for this policy.");
		}

		if (paymentRepository
				.existsByCustomerPolicyAndPaymentTypeAndStatus(
						customerPolicy,
						PaymentType.PURCHASE_PREMIUM,
						PaymentStatus.SUCCESS)) {

			throw new BadRequestException(
					"Policy premium already paid.");
		}

		// 4. Create Payment

		Payment payment = Payment.builder()

				.paymentNumber(
						generatePaymentNumber())

				.customer(customer)

				.customerPolicy(customerPolicy)

				.paymentType(
						request.getPaymentType())

				.paymentMethod(
						request.getPaymentMethod())

				.amount(
						request.getAmount())

				.status(
						PaymentStatus.PENDING)

				.remarks(
						request.getRemarks())

				.build();

		paymentRepository.save(payment);

		return mapToPaymentResponse(payment);

	}

	private String generatePaymentNumber() {

		return "PAY-"
				+ UUID.randomUUID()
						.toString()
						.substring(0, 8)
						.toUpperCase();

	}

	private PaymentResponse mapToPaymentResponse(
			Payment payment) {

		return PaymentResponse.builder()

				.paymentId(payment.getPaymentId())

				.paymentNumber(payment.getPaymentNumber())
				/*
				.customerId(
						payment.getCustomer()
								.getCustomerId())  */

				.customerPolicyId(
						payment.getCustomerPolicy()
								.getCustomerPolicyId())
				.paymentGateway(payment.getPaymentGateway())

				.paymentType(
						payment.getPaymentType())

				.paymentMethod(
						payment.getPaymentMethod())

				.amount(
						payment.getAmount())

				.status(
						payment.getStatus())

				.transactionReference(
						payment.getTransactionReference())

				.paymentDate(
						payment.getPaymentDate())

				.build();

	}

	@Transactional
	public PaymentResponse successPayment(
			Authentication authentication,
			Long paymentId) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Customer not found"));

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Payment not found"));

		// Security validation

		if (!payment.getCustomer()
				.getCustomerId()
				.equals(customer.getCustomerId())) {

			throw new BadRequestException(
					"Payment does not belong to customer");
		}

		// Status validation

		if (payment.getStatus() != PaymentStatus.PENDING) {

			throw new BadRequestException(
					"Only pending payments can be completed");
		}

		payment.setStatus(
				PaymentStatus.SUCCESS);

		activateCustomerPolicy(payment);

		payment.setTransactionReference(
				generateTransactionReference());

		payment.setPaymentDate(
				LocalDateTime.now());

		paymentRepository.save(payment);

		// notification
		notificationService.createNotification(
				payment.getCustomer(),
				"Payment Successful",
				"Your premium payment " + payment.getPaymentNumber() + " was completed successfully.",
				NotificationType.PAYMENT_SUCCESS,
				"PAYMENT",
				payment.getPaymentId());

		return mapToPaymentResponse(payment);

	}

	@Transactional
	public PaymentResponse failedPayment(
			Authentication authentication,
			Long paymentId) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Customer not found"));

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Payment not found"));

		// Validate ownership

		if (!payment.getCustomer()
				.getCustomerId()
				.equals(customer.getCustomerId())) {

			throw new BadRequestException(
					"Payment does not belong to customer");
		}

		// Validate status

		if (payment.getStatus() != PaymentStatus.PENDING) {

			throw new BadRequestException(
					"Only pending payments can be failed");
		}

		payment.setStatus(
				PaymentStatus.FAILED);

		payment.setTransactionReference(
				generateTransactionReference());

		payment.setPaymentDate(
				LocalDateTime.now());

		paymentRepository.save(payment);

		// notification
		notificationService.createNotification(
				customer,
				"Payment Failed",
				"Your payment " + payment.getPaymentNumber() + " has failed. Please try again.",
				NotificationType.PAYMENT_FAILED,
				"PAYMENT",
				payment.getPaymentId());

		return mapToPaymentResponse(payment);

	}

	public List<PaymentHistoryResponse> getMyPayments(
			Authentication authentication) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Customer not found"));

		List<Payment> payments = paymentRepository
				.findByCustomerCustomerId(
						customer.getCustomerId());

		return payments.stream()
				.map(this::mapToPaymentHistoryResponse)
				.toList();

	}

	private PaymentHistoryResponse mapToPaymentHistoryResponse(
			Payment payment) {

		return PaymentHistoryResponse.builder()

				.paymentId(
						payment.getPaymentId())

				.paymentNumber(
						payment.getPaymentNumber())

				/*	.customerPolicyId(
							payment.getCustomerPolicy() != null
									? payment.getCustomerPolicy()
											.getCustomerPolicyId()
									: null) */

				/*	.policyName(
							payment.getCustomerPolicy() != null
									? payment.getCustomerPolicy()
											.getPolicy()
											.getPolicyName()
									: null) */

				.paymentType(
						payment.getPaymentType())

				/*	.paymentMethod(
							payment.getPaymentMethod()) */

				.amount(
						payment.getAmount())

				.status(
						payment.getStatus())

				/*	.transactionReference(
							payment.getTransactionReference())
				
					.paymentDate(
							payment.getPaymentDate()) */

				.build();

	}

	public PaymentDetailsResponse getPaymentDetails(
			Authentication authentication,
			Long paymentId) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		Payment payment = paymentRepository
				.findByPaymentIdAndCustomer(paymentId, customer)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

		return mapToPaymentDetailsResponse(payment);
	}

	private PaymentDetailsResponse mapToPaymentDetailsResponse(
			Payment payment) {

		Customer customer = payment.getCustomer();

		CustomerPolicy customerPolicy = payment.getCustomerPolicy();

		return PaymentDetailsResponse.builder()

				.paymentId(payment.getPaymentId())

				.paymentNumber(payment.getPaymentNumber())

				.customerId(customer.getCustomerId())

				.customerName(customer.getFirstName()
						+ " "
						+ customer.getLastName())

				.customerPolicyId(customerPolicy.getCustomerPolicyId())

				.policyNumber(customerPolicy.getPolicyNumber())

				.policyName(customerPolicy.getPolicy().getPolicyName())

				.paymentType(payment.getPaymentType())

				.paymentMethod(payment.getPaymentMethod())

				.amount(payment.getAmount())

				.status(payment.getStatus())

				.transactionReference(payment.getTransactionReference())

				.paymentDate(payment.getPaymentDate())

				.remarks(payment.getRemarks())

				.build();
	}

	private String generateTransactionReference() {

		return "TXN-"
				+ UUID.randomUUID()
						.toString()
						.substring(0, 8)
						.toUpperCase();
	}

	public List<AdminPaymentResponse> getAllPayments() {

		List<Payment> payments = paymentRepository.findAllByOrderByCreatedAtDesc();

		return payments.stream()
				.map(this::mapToAdminPaymentResponse)
				.toList();
	}

	private AdminPaymentResponse mapToAdminPaymentResponse(
			Payment payment) {

		Customer customer = payment.getCustomer();

		CustomerPolicy customerPolicy = payment.getCustomerPolicy();

		return AdminPaymentResponse.builder()

				.paymentId(payment.getPaymentId())

				.paymentNumber(payment.getPaymentNumber())

				.customerId(customer.getCustomerId())

				.customerName(
						customer.getFirstName()
								+ " "
								+ customer.getLastName())

				.customerPolicyId(
						customerPolicy != null
								? customerPolicy.getCustomerPolicyId()
								: null)

				.policyNumber(
						customerPolicy != null
								? customerPolicy.getPolicyNumber()
								: null)

				.policyName(
						customerPolicy != null
								? customerPolicy.getPolicy().getPolicyName()
								: null)

				.paymentType(payment.getPaymentType())

				.paymentMethod(payment.getPaymentMethod())

				.amount(payment.getAmount())

				.status(payment.getStatus())

				.transactionReference(
						payment.getTransactionReference())

				.paymentDate(payment.getPaymentDate())

				.build();
	}

	public AdminPaymentDetailsResponse getPaymentDetails(
			Long paymentId) {

		Payment payment = paymentRepository
				.findByPaymentId(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Payment not found"));

		return mapToAdminPaymentDetailsResponse(payment);

	}

	private AdminPaymentDetailsResponse mapToAdminPaymentDetailsResponse(
			Payment payment) {

		Customer customer = payment.getCustomer();

		CustomerPolicy customerPolicy = payment.getCustomerPolicy();

		return AdminPaymentDetailsResponse.builder()

				/*
				 * Payment
				 */
				.paymentId(
						payment.getPaymentId())

				.paymentNumber(
						payment.getPaymentNumber())

				.paymentType(
						payment.getPaymentType())

				.paymentMethod(
						payment.getPaymentMethod())

				.amount(
						payment.getAmount())

				.status(
						payment.getStatus())

				.transactionReference(
						payment.getTransactionReference())

				.paymentDate(
						payment.getPaymentDate())

				.remarks(
						payment.getRemarks())

				/*
				 * Customer
				 */
				.customerId(
						customer.getCustomerId())

				.customerName(
						customer.getFirstName()
								+ " "
								+ customer.getLastName())

				.email(
						customer.getUser().getEmail())

				.mobileNumber(
						customer.getUser().getPhone())

				/*
				 * Policy
				 */
				.customerPolicyId(
						customerPolicy != null
								? customerPolicy.getCustomerPolicyId()
								: null)

				.policyNumber(
						customerPolicy != null
								? customerPolicy.getPolicyNumber()
								: null)

				.policyId(
						customerPolicy != null
								? customerPolicy.getPolicy().getPolicyId()
								: null)

				.policyName(
						customerPolicy != null
								? customerPolicy.getPolicy().getPolicyName()
								: null)

				/*
				 * Future Claim Support
				 */
				.claimId(
						payment.getClaim() != null
								? payment.getClaim().getClaimId()
								: null)

				.claimNumber(
						payment.getClaim() != null
								? payment.getClaim().getClaimNumber()
								: null)

				/*
				 * Audit
				 */
				.createdAt(
						payment.getCreatedAt())

				.updatedAt(
						payment.getUpdatedAt())

				.build();
	}

	@Transactional
	public RefundPaymentResponse refundPayment(

			Long paymentId,

			RefundPaymentRequest request) {

		Payment payment = paymentRepository
				.findByPaymentId(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Payment not found"));

		validatePaymentForRefund(payment);

		payment.setStatus(
				PaymentStatus.REFUNDED);

		payment.setRefundReference(
				paymentReferenceGenerator.generateRefundReference());

		payment.setRefundDate(
				LocalDateTime.now());

		payment.setRefundRemarks(
				request.getRefundRemarks());

		paymentRepository.save(payment);

		// notification
		notificationService.createNotification(

				payment.getCustomer(),

				"Payment Refunded",

				"Refund has been processed for payment "
						+ payment.getPaymentNumber()
						+ ".",

				NotificationType.PAYMENT_REFUNDED,

				"PAYMENT",

				payment.getPaymentId()

		);

		return mapToRefundPaymentResponse(payment);
	}

	private void validatePaymentForRefund(
			Payment payment) {

		if (payment.getStatus() == PaymentStatus.REFUNDED) {

			throw new BadRequestException(
					"Payment is already refunded.");
		}

		if (payment.getStatus() == PaymentStatus.PENDING) {

			throw new BadRequestException(
					"Pending payments cannot be refunded.");
		}

		if (payment.getStatus() == PaymentStatus.FAILED) {

			throw new BadRequestException(
					"Failed payments cannot be refunded.");
		}

		if (payment.getStatus() != PaymentStatus.SUCCESS) {

			throw new BadRequestException(
					"Only successful payments can be refunded.");
		}

	}

	private RefundPaymentResponse mapToRefundPaymentResponse(
			Payment payment) {

		return RefundPaymentResponse.builder()

				.paymentId(
						payment.getPaymentId())

				.paymentNumber(
						payment.getPaymentNumber())

				.status(
						payment.getStatus())

				.refundReference(
						payment.getRefundReference())

				.refundDate(
						payment.getRefundDate())

				.refundRemarks(
						payment.getRefundRemarks())

				.message(
						"Payment refunded successfully.")

				.build();
	}

	public List<PremiumHistoryResponse> getPremiumHistory(
			Authentication authentication,
			Long customerPolicyId) {

		String username = authentication.getName();

		Customer customer = customerRepository.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

		CustomerPolicy customerPolicy = customerPolicyRepository
				.findByCustomerPolicyIdAndCustomer(customerPolicyId, customer)
				.orElseThrow(() -> new ResourceNotFoundException("Customer policy not found."));

		List<Payment> payments = paymentRepository
				.findByCustomerPolicyOrderByPaymentDateDesc(customerPolicy);

		return payments.stream()
				.map(this::mapToPremiumHistoryResponse)
				.toList();
	}

	private PremiumHistoryResponse mapToPremiumHistoryResponse(Payment payment) {

		PremiumHistoryResponse response = new PremiumHistoryResponse();

		response.setPaymentId(payment.getPaymentId());
		response.setPaymentNumber(payment.getPaymentNumber());

		response.setCustomerPolicyId(
				payment.getCustomerPolicy().getCustomerPolicyId());

		response.setPolicyName(
				payment.getCustomerPolicy().getPolicy().getPolicyName());

		response.setPaymentType(payment.getPaymentType());
		response.setPaymentMethod(payment.getPaymentMethod());
		response.setAmount(payment.getAmount());
		response.setStatus(payment.getStatus());
		response.setTransactionReference(payment.getTransactionReference());
		response.setPaymentDate(payment.getPaymentDate());
		response.setRemarks(payment.getRemarks());

		return response;
	}

	public CustomerPaymentResponse getCustomerPaymentDetails(
			Authentication authentication,
			Long paymentId) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found."));

		if (!payment.getCustomer().getCustomerId()
				.equals(customer.getCustomerId())) {

			throw new ResourceNotFoundException("Payment not found.");
		}

		return mapToCustomerPaymentResponse(payment);
	}

	private CustomerPaymentResponse mapToCustomerPaymentResponse(
			Payment payment) {

		CustomerPaymentResponse response = new CustomerPaymentResponse();

		response.setPaymentId(payment.getPaymentId());
		response.setPaymentNumber(payment.getPaymentNumber());

		response.setPaymentType(payment.getPaymentType());
		response.setPaymentMethod(payment.getPaymentMethod());

		response.setAmount(payment.getAmount());
		response.setStatus(payment.getStatus());

		response.setTransactionReference(payment.getTransactionReference());
		response.setPaymentDate(payment.getPaymentDate());
		response.setRemarks(payment.getRemarks());

		response.setCustomerPolicyId(
				payment.getCustomerPolicy().getCustomerPolicyId());

		response.setPolicyNumber(
				payment.getCustomerPolicy().getPolicyNumber());

		response.setPolicyName(
				payment.getCustomerPolicy().getPolicy().getPolicyName());

		response.setCustomerId(
				payment.getCustomer().getCustomerId());

		response.setCustomerName(
				payment.getCustomer().getFirstName() + " " + payment.getCustomer().getLastName());

		return response;
	}

	private void activateCustomerPolicy(Payment payment) {

		CustomerPolicy customerPolicy = payment.getCustomerPolicy();

		customerPolicy.setStatus(
				CustomerPolicyStatus.ACTIVE);

		customerPolicyRepository.save(customerPolicy);

		premiumPaymentScheduleService
				.generatePaymentSchedules(customerPolicy);
	}

	@Transactional
	public ClaimPaymentResponse processClaimPayment(Long claimId, ClaimPaymentRequest request) {

		// Validate claim
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim not found."));

		// Only approved claims can be paid
		if (claim.getStatus() != ClaimStatus.APPROVED) {
			throw new BadRequestException("Only approved claims can be processed for payment.");
		}

		// Prevent duplicate payment
		if (paymentRepository.existsByClaimClaimId(claimId)) {
			throw new BadRequestException("Claim payment has already been processed.");
		}

		// Approved amount must exist
		if (claim.getApprovedAmount() == null
				|| claim.getApprovedAmount().compareTo(BigDecimal.ZERO) <= 0) {

			throw new BadRequestException("Approved amount must be greater than zero.");
		}

		// Create payment
		Payment payment = Payment.builder()
				.paymentNumber(generatePaymentNumber())
				.customer(claim.getCustomerPolicy().getCustomer())
				.customerPolicy(claim.getCustomerPolicy())
				.claim(claim)
				.paymentType(PaymentType.CLAIM_SETTLEMENT)
				.paymentMethod(request.getPaymentMethod())
				.paymentGateway(request.getPaymentGateway())
				.amount(claim.getApprovedAmount())
				.status(PaymentStatus.SUCCESS)
				.transactionReference(request.getTransactionReference())
				.paymentDate(LocalDateTime.now())
				.remarks(request.getRemarks())
				.build();

		paymentRepository.save(payment);

		// Update claim
		claim.setStatus(ClaimStatus.SETTLED);
		claimRepository.save(claim);

		// Create notification
		createClaimPaymentNotification(claim, payment);

		return mapToClaimPaymentResponse(payment);
	}

	private void createClaimPaymentNotification(Claim claim, Payment payment) {

		Notification notification = Notification.builder()
				.customer(payment.getCustomer())
				.title("Claim Payment Successful")
				.message(String.format(
						"Your claim %s has been settled successfully. Amount ₹%s has been credited.",
						payment.getClaim().getClaimNumber(),
						payment.getAmount()))
				.notificationType(NotificationType.CLAIM_PAYMENT_SUCCESS)
				.status(NotificationStatus.UNREAD)
				.referenceType(ReferenceType.CLAIM.name())
				.referenceId(payment.getClaim().getClaimId())
				.build();

		notificationRepository.save(notification);
	}

	private ClaimPaymentResponse mapToClaimPaymentResponse(Payment payment) {

		return ClaimPaymentResponse.builder()
				.paymentId(payment.getPaymentId())
				.paymentNumber(payment.getPaymentNumber())
				.customerPolicyId(payment.getCustomerPolicy().getCustomerPolicyId())
				.claimId(payment.getClaim().getClaimId())
				.paymentType(payment.getPaymentType())
				.paymentMethod(payment.getPaymentMethod())
				.paymentGateway(payment.getPaymentGateway())
				.amount(payment.getAmount())
				.status(payment.getStatus())
				.transactionReference(payment.getTransactionReference())
				.paymentDate(payment.getPaymentDate())
				.remarks(payment.getRemarks())
				.build();
	}

	public ClaimPaymentResponse getClaimPayment(Long claimId) {

		Claim claim = claimRepository.findByClaimId(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim not found."));

		Payment payment = paymentRepository.findByClaim(claim)
				.orElseThrow(() -> new ResourceNotFoundException("Claim payment not found."));

		ClaimPaymentResponse response = new ClaimPaymentResponse();

		response.setPaymentId(payment.getPaymentId());
		response.setPaymentNumber(payment.getPaymentNumber());

		response.setClaimId(claim.getClaimId());

		response.setPaymentType(payment.getPaymentType());
		response.setPaymentMethod(payment.getPaymentMethod());
		response.setPaymentGateway(payment.getPaymentGateway());

		response.setAmount(payment.getAmount());

		response.setStatus(payment.getStatus());
		response.setTransactionReference(payment.getTransactionReference());

		response.setPaymentDate(payment.getPaymentDate());

		response.setRemarks(payment.getRemarks());

		return response;
	}

	public ClaimPaymentResponse getCustomerClaimPayment(Long claimId) {

		String username = SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));

		Customer customer = customerRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

		Claim claim = claimRepository
				.findByClaimIdAndCustomerPolicyCustomer(claimId, customer)
				.orElseThrow(() -> new ResourceNotFoundException("Claim not found."));

		Payment payment = paymentRepository.findByClaim(claim)
				.orElseThrow(() -> new ResourceNotFoundException("Claim payment not found."));

		ClaimPaymentResponse response = new ClaimPaymentResponse();

		response.setPaymentId(payment.getPaymentId());
		response.setPaymentNumber(payment.getPaymentNumber());
		response.setClaimId(claim.getClaimId());
		response.setCustomerPolicyId(claim.getCustomerPolicy().getCustomerPolicyId());
		response.setPaymentType(payment.getPaymentType());
		response.setPaymentMethod(payment.getPaymentMethod());
		response.setPaymentGateway(payment.getPaymentGateway());
		response.setAmount(payment.getAmount());
		response.setStatus(payment.getStatus());
		response.setTransactionReference(payment.getTransactionReference());
		response.setPaymentDate(payment.getPaymentDate());
		response.setRemarks(payment.getRemarks());

		return response;
	}

}