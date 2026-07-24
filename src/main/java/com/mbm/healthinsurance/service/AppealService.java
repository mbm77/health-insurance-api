package com.mbm.healthinsurance.service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.healthinsurance.dto.request.CreateAppealRequest;
import com.mbm.healthinsurance.dto.request.ResolveAppealRequest;
import com.mbm.healthinsurance.dto.response.AdminAppealDetailsResponse;
import com.mbm.healthinsurance.dto.response.AdminAppealResponse;
import com.mbm.healthinsurance.dto.response.AppealDetailsResponse;
import com.mbm.healthinsurance.dto.response.AppealResponse;
import com.mbm.healthinsurance.dto.response.AppealSummaryResponse;
import com.mbm.healthinsurance.dto.response.RejectAppealRequest;
import com.mbm.healthinsurance.entity.Appeal;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.Notification;
import com.mbm.healthinsurance.entity.User;
import com.mbm.healthinsurance.enums.AppealStatus;
import com.mbm.healthinsurance.enums.NotificationStatus;
import com.mbm.healthinsurance.enums.NotificationType;
import com.mbm.healthinsurance.enums.ReferenceType;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.AppealRepository;
import com.mbm.healthinsurance.repository.ClaimRepository;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.NotificationRepository;
import com.mbm.healthinsurance.repository.PaymentRepository;
import com.mbm.healthinsurance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppealService {

	private final AppealRepository appealRepository;
	private final CustomerRepository customerRepository;
	private final ClaimRepository claimRepository;
	private final PaymentRepository paymentRepository;
	private final CustomerPolicyRepository customerPolicyRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;

	@Transactional
	public AppealResponse createAppeal(Authentication authentication,
			CreateAppealRequest request) {

		Customer customer = getLoggedInCustomer(authentication);

		validateReference(customer, request);

		Appeal appeal = Appeal.builder()
				.appealNumber(generateAppealNumber())
				.customer(customer)
				.category(request.getCategory())
				.subject(request.getSubject().trim())
				.description(request.getDescription().trim())
				.referenceType(request.getReferenceType())
				.referenceId(request.getReferenceId())
				.status(AppealStatus.SUBMITTED)
				.build();

		appealRepository.save(appeal);

		return AppealResponse.builder()
				.appealId(appeal.getAppealId())
				.appealNumber(appeal.getAppealNumber())
				.category(appeal.getCategory())
				.status(appeal.getStatus())
				.message("Appeal submitted successfully.")
				.build();
	}

	@Transactional(readOnly = true)
	public List<AppealSummaryResponse> getMyAppeals(Authentication authentication) {

		Customer customer = getLoggedInCustomer(authentication);

		List<Appeal> appeals = appealRepository
				.findByCustomerCustomerIdOrderByCreatedAtDesc(customer.getCustomerId());

		if (appeals.isEmpty()) {
			throw new ResourceNotFoundException("No appeals found.");
		}

		return appeals.stream()
				.map(appeal -> AppealSummaryResponse.builder()
						.appealId(appeal.getAppealId())
						.appealNumber(appeal.getAppealNumber())
						.category(appeal.getCategory())
						.subject(appeal.getSubject())
						.status(appeal.getStatus())
						.createdAt(appeal.getCreatedAt())
						.build())
				.toList();
	}

	@Transactional(readOnly = true)
	public AppealDetailsResponse getMyAppealById(Authentication authentication,
			Long appealId) {

		Customer customer = getLoggedInCustomer(authentication);

		Appeal appeal = appealRepository
				.findByAppealIdAndCustomerCustomerId(
						appealId,
						customer.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Appeal not found."));

		return AppealDetailsResponse.builder()
				.appealId(appeal.getAppealId())
				.appealNumber(appeal.getAppealNumber())
				.category(appeal.getCategory())
				.subject(appeal.getSubject())
				.description(appeal.getDescription())
				.referenceType(appeal.getReferenceType())
				.referenceId(appeal.getReferenceId())
				.status(appeal.getStatus())
				.adminRemarks(appeal.getAdminRemarks())
				.createdAt(appeal.getCreatedAt())
				.updatedAt(appeal.getUpdatedAt())
				.build();
	}

	@Transactional(readOnly = true)
	public List<AdminAppealResponse> getAllAppeals() {

		List<Appeal> appeals = appealRepository.findAllByOrderByCreatedAtDesc();

		if (appeals.isEmpty()) {
			throw new ResourceNotFoundException("No appeals found.");
		}

		return appeals.stream()
				.map(appeal -> AdminAppealResponse.builder()
						.appealId(appeal.getAppealId())
						.appealNumber(appeal.getAppealNumber())
						.customerId(appeal.getCustomer().getCustomerId())
						.customerName(appeal.getCustomer().getFirstName() + " " + appeal.getCustomer().getLastName())
						.category(appeal.getCategory())
						.subject(appeal.getSubject())
						.status(appeal.getStatus())
						.createdAt(appeal.getCreatedAt())
						.build())
				.toList();
	}

	@Transactional(readOnly = true)
	public AdminAppealDetailsResponse getAppealById(Long appealId) {

		Appeal appeal = appealRepository.findById(appealId)
				.orElseThrow(() -> new ResourceNotFoundException("Appeal not found."));

		return AdminAppealDetailsResponse.builder()
				.appealId(appeal.getAppealId())
				.appealNumber(appeal.getAppealNumber())
				.customerId(appeal.getCustomer().getCustomerId())
				.customerName(appeal.getCustomer().getFirstName() + "" + appeal.getCustomer().getLastName())
				.category(appeal.getCategory())
				.subject(appeal.getSubject())
				.description(appeal.getDescription())
				.referenceType(appeal.getReferenceType())
				.referenceId(appeal.getReferenceId())
				.status(appeal.getStatus())
				.adminRemarks(appeal.getAdminRemarks())
				.createdAt(appeal.getCreatedAt())
				.updatedAt(appeal.getUpdatedAt())
				.build();
	}

	@Transactional
	public AppealResponse reviewAppeal(Long appealId) {

		Appeal appeal = appealRepository.findById(appealId)
				.orElseThrow(() -> new ResourceNotFoundException("Appeal not found."));

		if (appeal.getStatus() != AppealStatus.SUBMITTED) {
			throw new BadRequestException(
					"Only submitted appeals can be moved to under review.");
		}

		appeal.setStatus(AppealStatus.UNDER_REVIEW);

		appealRepository.save(appeal);

		return AppealResponse.builder()
				.appealId(appeal.getAppealId())
				.appealNumber(appeal.getAppealNumber())
				.category(appeal.getCategory())
				.status(appeal.getStatus())
				.message("Appeal is now under review.")
				.build();
	}

	@Transactional
	public AppealResponse resolveAppeal(Long appealId,
			ResolveAppealRequest request) {

		Appeal appeal = appealRepository.findById(appealId)
				.orElseThrow(() -> new ResourceNotFoundException("Appeal not found."));

		if (appeal.getStatus() != AppealStatus.UNDER_REVIEW) {
			throw new BadRequestException(
					"Only appeals under review can be resolved.");
		}

		appeal.setStatus(AppealStatus.RESOLVED);
		appeal.setAdminRemarks(request.getAdminRemarks());

		appealRepository.save(appeal);

		createAppealResolvedNotification(appeal);

		return AppealResponse.builder()
				.appealId(appeal.getAppealId())
				.appealNumber(appeal.getAppealNumber())
				.category(appeal.getCategory())
				.status(appeal.getStatus())
				.message("Appeal resolved successfully.")
				.build();
	}

	@Transactional
	public AppealResponse rejectAppeal(Long appealId,
			RejectAppealRequest request) {

		Appeal appeal = appealRepository.findById(appealId)
				.orElseThrow(() -> new ResourceNotFoundException("Appeal not found."));

		if (appeal.getStatus() != AppealStatus.UNDER_REVIEW) {
			throw new BadRequestException(
					"Only appeals under review can be rejected.");
		}

		appeal.setStatus(AppealStatus.REJECTED);
		appeal.setAdminRemarks(request.getAdminRemarks());

		appealRepository.save(appeal);

		createAppealRejectedNotification(appeal);

		return AppealResponse.builder()
				.appealId(appeal.getAppealId())
				.appealNumber(appeal.getAppealNumber())
				.category(appeal.getCategory())
				.status(appeal.getStatus())
				.message("Appeal rejected successfully.")
				.build();
	}

	private void validateReference(Customer customer,
			CreateAppealRequest request) {

		if (request.getReferenceType() == null && request.getReferenceId() != null) {
			throw new BadRequestException("Reference type is required.");
		}

		if (request.getReferenceType() != null && request.getReferenceId() == null) {
			throw new BadRequestException("Reference ID is required.");
		}

		if (request.getReferenceType() == null) {
			return;
		}

		switch (request.getReferenceType()) {

		case CLAIM -> {

			claimRepository.findByClaimIdAndCustomerPolicyCustomerCustomerId(
					request.getReferenceId(),
					customer.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Claim not found."));
		}

		case PAYMENT -> {

			paymentRepository.findByPaymentIdAndCustomerCustomerId(
					request.getReferenceId(),
					customer.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Payment not found."));
		}

		case POLICY -> {

			customerPolicyRepository
					.findByCustomerPolicyIdAndCustomerCustomerId(
							request.getReferenceId(),
							customer.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Customer Policy not found."));
		}

		default -> {
			// No validation required for OTHER or future types
		}
		}
	}

	private String generateAppealNumber() {

		String appealNumber;

		do {
			appealNumber = "APL-" + Year.now().getValue() + "-"
					+ UUID.randomUUID().toString().substring(0, 6).toUpperCase();
		} while (appealRepository.existsByAppealNumber(appealNumber));

		return appealNumber;
	}

	private Customer getLoggedInCustomer(Authentication authentication) {

		String username = authentication.getName();

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));

		return customerRepository.findByUserUserId(user.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
	}

	private void createAppealResolvedNotification(Appeal appeal) {

		Notification notification = Notification.builder()
				.customer(appeal.getCustomer())
				.title("Appeal Resolved")
				.message("Your appeal " + appeal.getAppealNumber()
						+ " has been resolved successfully.")
				.notificationType(NotificationType.APPEAL)
				.referenceType(ReferenceType.APPEAL.name())
				.referenceId(appeal.getAppealId())
				.status(NotificationStatus.UNREAD)
				.build();

		notificationRepository.save(notification);
	}

	private void createAppealRejectedNotification(Appeal appeal) {

		Notification notification = Notification.builder()
				.customer(appeal.getCustomer())
				.title("Appeal Rejected")
				.message("Your appeal " + appeal.getAppealNumber()
						+ " has been rejected. Please review the admin remarks for more details.")
				.notificationType(NotificationType.APPEAL)
				.referenceType(ReferenceType.APPEAL.name())
				.referenceId(appeal.getAppealId())
				.status(NotificationStatus.UNREAD)
				.build();

		notificationRepository.save(notification);
	}

}