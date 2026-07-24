package com.mbm.healthinsurance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.healthinsurance.dto.request.ApproveClaimRequest;
import com.mbm.healthinsurance.dto.request.ClaimRequest;
import com.mbm.healthinsurance.dto.request.RejectClaimRequest;
import com.mbm.healthinsurance.dto.response.AdminClaimDetailsResponse;
import com.mbm.healthinsurance.dto.response.AdminClaimsResponse;
import com.mbm.healthinsurance.dto.response.ApproveClaimResponse;
import com.mbm.healthinsurance.dto.response.ClaimDetailsResponse;
import com.mbm.healthinsurance.dto.response.ClaimResponse;
import com.mbm.healthinsurance.dto.response.MyClaimsResponse;
import com.mbm.healthinsurance.dto.response.RejectClaimResponse;
import com.mbm.healthinsurance.entity.Claim;
import com.mbm.healthinsurance.entity.ClaimSequence;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.enums.ClaimStatus;
import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.NotificationType;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.repository.ClaimRepository;
import com.mbm.healthinsurance.repository.ClaimSequenceRepository;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClaimService {

	private final CustomerRepository customerRepository;

	private final CustomerPolicyRepository customerPolicyRepository;

	private final ClaimRepository claimRepository;
	private final ClaimSequenceRepository claimSequenceRepository;
	private final NotificationService notificationService;

	@Transactional
	public ClaimResponse raiseClaim(
			Authentication authentication,
			Long customerPolicyId,
			ClaimRequest request) {

		// String claimNumber = generateClaimNumber();

		// 1. Get logged-in customer
		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// 2. Find purchased policy
		CustomerPolicy customerPolicy = customerPolicyRepository
				.findByCustomerPolicyIdAndCustomer(
						customerPolicyId,
						customer)
				.orElseThrow(() -> new RuntimeException(
						"Purchased policy not found"));

		// 3. Validate policy status
		if (customerPolicy.getStatus() != CustomerPolicyStatus.ACTIVE) {

			throw new RuntimeException(
					"Claim can be raised only for active policies.");
		}

		// 4. Validate claim dates
		if (request.getDischargeDate()
				.isBefore(request.getAdmissionDate())) {

			throw new RuntimeException(
					"Discharge date cannot be before admission date.");
		}

		// 5. Create Claim
		Claim claim = Claim.builder()
				.customerPolicy(customerPolicy)
				.claimNumber(generateClaimNumber())
				.hospitalName(request.getHospitalName())
				.admissionDate(request.getAdmissionDate())
				.dischargeDate(request.getDischargeDate())
				.claimAmount(request.getClaimAmount())
				.claimReason(request.getClaimReason())
				.status(ClaimStatus.SUBMITTED)
				.build();

		// 6. Save Claim
		claimRepository.save(claim);

		// notification
		notificationService.createNotification(

				customer,

				"Claim Submitted",

				"Your claim "
						+ claim.getClaimNumber()
						+ " has been submitted successfully.",

				NotificationType.CLAIM_SUBMITTED,

				"CLAIM",

				claim.getClaimId()

		);

		// 7. Return response
		return mapToClaimResponse(claim);
	}

	public List<MyClaimsResponse> getMyClaims(
			Authentication authentication) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new RuntimeException(
						"Customer not found"));

		List<Claim> claims = claimRepository
				.findByCustomerPolicyCustomer(customer);

		return claims.stream()
				.map(this::mapToMyClaimsResponse)
				.toList();

	}

	public ClaimDetailsResponse getClaimDetails(
			Authentication authentication,
			Long claimId) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		Claim claim = claimRepository
				.findByClaimIdAndCustomerPolicyCustomer(
						claimId,
						customer)
				.orElseThrow(() -> new RuntimeException("Claim not found"));

		return mapToClaimDetailsResponse(claim);
	}

	public List<AdminClaimsResponse> getAllClaims() {

		List<Claim> claims = claimRepository.findAll();

		return claims.stream()
				.map(this::mapToAdminClaimsResponse)
				.toList();
	}

	public AdminClaimDetailsResponse getClaimDetails(
			Long claimId) {

		Claim claim = claimRepository
				.findById(claimId)
				.orElseThrow(() -> new RuntimeException("Claim not found"));

		return mapToAdminClaimDetailsResponse(claim);
	}

	@Transactional
	public ApproveClaimResponse approveClaim(
			Long claimId,
			ApproveClaimRequest request) {

		Claim claim = claimRepository
				.findById(claimId)
				.orElseThrow(() -> new RuntimeException("Claim not found"));

		validateClaimForApproval(claim);

		claim.setApprovedAmount(
				request.getApprovedAmount());

		claim.setRemarks(
				request.getRemarks());

		claim.setStatus(
				ClaimStatus.APPROVED);

		claimRepository.save(claim);

		// notification
		notificationService.createNotification(

				claim.getCustomerPolicy().getCustomer(),

				"Claim Approved",

				"Your claim " + claim.getClaimNumber()
						+ " has been approved successfully. The approved amount will be processed shortly.",

				NotificationType.CLAIM_APPROVED,

				"CLAIM",

				claim.getClaimId()

		);

		return ApproveClaimResponse.builder()

				.claimId(claim.getClaimId())

				.claimNumber(claim.getClaimNumber())

				.approvedAmount(claim.getApprovedAmount())

				.status(claim.getStatus())

				.remarks(claim.getRemarks())

				.message("Claim approved successfully.")

				.build();
	}

	@Transactional
	public RejectClaimResponse rejectClaim(
			Long claimId,
			RejectClaimRequest request) {

		Claim claim = claimRepository
				.findById(claimId)
				.orElseThrow(() -> new RuntimeException("Claim not found."));

		validateClaimForRejection(claim);

		claim.setStatus(ClaimStatus.REJECTED);

		claim.setRemarks(request.getRemarks());

		claimRepository.save(claim);

		// notification
		notificationService.createNotification(

				claim.getCustomerPolicy().getCustomer(),

				"Claim Rejected",

				"Your claim " + claim.getClaimNumber()
						+ " has been rejected. Please review the remarks or contact customer support for further assistance.",

				NotificationType.CLAIM_REJECTED,

				"CLAIM",

				claim.getClaimId()

		);

		return RejectClaimResponse.builder()
				.claimId(claim.getClaimId())
				.claimNumber(claim.getClaimNumber())
				.status(claim.getStatus())
				.remarks(claim.getRemarks())
				.message("Claim rejected successfully.")
				.build();
	}

	private void validateClaimForRejection(
			Claim claim) {

		if (claim.getStatus() == ClaimStatus.REJECTED) {

			throw new BadRequestException(
					"Claim is already rejected.");
		}

		if (claim.getStatus() == ClaimStatus.APPROVED) {

			throw new BadRequestException(
					"Approved claims cannot be rejected.");
		}

		if (claim.getStatus() != ClaimStatus.SUBMITTED) {

			throw new BadRequestException(
					"Only submitted claims can be rejected.");
		}

	}

	private void validateClaimForApproval(Claim claim) {

		if (claim.getStatus() == ClaimStatus.APPROVED) {
			throw new BadRequestException("Claim is already approved.");
		}

		if (claim.getStatus() == ClaimStatus.REJECTED) {
			throw new BadRequestException("Rejected claims cannot be approved.");
		}

		if (claim.getStatus() != ClaimStatus.SUBMITTED) {
			throw new BadRequestException("Only submitted claims can be approved.");
		}
	}

	private AdminClaimDetailsResponse mapToAdminClaimDetailsResponse(
			Claim claim) {

		CustomerPolicy customerPolicy = claim.getCustomerPolicy();

		Customer customer = customerPolicy.getCustomer();

		return AdminClaimDetailsResponse.builder()

				// Claim
				.claimId(claim.getClaimId())
				.claimNumber(claim.getClaimNumber())
				.status(claim.getStatus())

				// Customer
				.customerId(customer.getCustomerId())
				.customerName(customer.getFirstName() + " " + customer.getLastName())
				.email(customer.getUser().getEmail())
				.mobileNumber(customer.getUser().getPhone())

				// Policy
				.customerPolicyId(customerPolicy.getCustomerPolicyId())
				.policyId(customerPolicy.getPolicy().getPolicyId())
				.policyName(customerPolicy.getPolicy().getPolicyName())

				// Hospital
				.hospitalName(claim.getHospitalName())
				.admissionDate(claim.getAdmissionDate())
				.dischargeDate(claim.getDischargeDate())
				.claimReason(claim.getClaimReason())

				// Financial
				.claimAmount(claim.getClaimAmount())
				.approvedAmount(claim.getApprovedAmount())

				// Review
				.remarks(claim.getRemarks())

				// Audit
				.createdAt(claim.getCreatedAt())

				.build();
	}

	private AdminClaimsResponse mapToAdminClaimsResponse(
			Claim claim) {

		CustomerPolicy customerPolicy = claim.getCustomerPolicy();

		Customer customer = customerPolicy.getCustomer();

		return AdminClaimsResponse.builder()

				// Claim
				.claimId(claim.getClaimId())
				.claimNumber(claim.getClaimNumber())
				.status(claim.getStatus())

				// Customer
				.customerId(customer.getCustomerId())
				.customerName(customer.getFirstName() + " " + customer.getLastName())

				// Policy
				.customerPolicyId(customerPolicy.getCustomerPolicyId())
				.policyId(customerPolicy.getPolicy().getPolicyId())
				.policyName(customerPolicy.getPolicy().getPolicyName())

				// Hospital
				.hospitalName(claim.getHospitalName())
				.admissionDate(claim.getAdmissionDate())
				.dischargeDate(claim.getDischargeDate())

				// Financial
				.claimAmount(claim.getClaimAmount())
				.approvedAmount(claim.getApprovedAmount())

				// Audit
				.createdAt(claim.getCreatedAt())

				.build();
	}

	private String generateClaimNumber() {

		int year = LocalDate.now().getYear();

		ClaimSequence sequence = claimSequenceRepository
				.findBySequenceYear(year)
				.orElseGet(() -> {

					ClaimSequence newSequence = new ClaimSequence();

					newSequence.setSequenceYear(year);
					newSequence.setCurrentValue(0L);

					return newSequence;
				});

		long nextValue = sequence.getCurrentValue() + 1;

		sequence.setCurrentValue(nextValue);

		claimSequenceRepository.save(sequence);

		return String.format(
				"CLM-%d-%06d",
				year,
				nextValue);
	}

	private ClaimResponse mapToClaimResponse(
			Claim claim) {

		return ClaimResponse.builder()
				.claimId(claim.getClaimId())
				.claimNumber(claim.getClaimNumber())
				.claimAmount(claim.getClaimAmount())
				.status(claim.getStatus())
				.message("Claim submitted successfully.")
				.build();
	}

	private MyClaimsResponse mapToMyClaimsResponse(
			Claim claim) {

		CustomerPolicy customerPolicy = claim.getCustomerPolicy();

		return MyClaimsResponse.builder()

				.claimId(claim.getClaimId())

				.claimNumber(
						claim.getClaimNumber())

				.claimAmount(
						claim.getClaimAmount())

				.approvedAmount(
						claim.getApprovedAmount())

				.status(
						claim.getStatus())

				.customerPolicyId(
						customerPolicy.getCustomerPolicyId())

				.policyId(
						customerPolicy.getPolicy().getPolicyId())

				.policyName(
						customerPolicy.getPolicy().getPolicyName())

				.hospitalName(
						claim.getHospitalName())

				.admissionDate(
						claim.getAdmissionDate())

				.dischargeDate(
						claim.getDischargeDate())

				.claimReason(
						claim.getClaimReason())

				.remarks(
						claim.getRemarks())

				.createdAt(
						claim.getCreatedAt())

				.build();
	}

	private ClaimDetailsResponse mapToClaimDetailsResponse(
			Claim claim) {

		CustomerPolicy customerPolicy = claim.getCustomerPolicy();

		return ClaimDetailsResponse.builder()

				// Claim
				.claimId(claim.getClaimId())
				.claimNumber(claim.getClaimNumber())
				.status(claim.getStatus())

				// Policy
				.customerPolicyId(customerPolicy.getCustomerPolicyId())
				.policyId(customerPolicy.getPolicy().getPolicyId())
				.policyName(customerPolicy.getPolicy().getPolicyName())

				// Hospital
				.hospitalName(claim.getHospitalName())
				.admissionDate(claim.getAdmissionDate())
				.dischargeDate(claim.getDischargeDate())
				.claimReason(claim.getClaimReason())

				// Financial
				.claimAmount(claim.getClaimAmount())
				.approvedAmount(claim.getApprovedAmount())

				// Review
				.remarks(claim.getRemarks())

				// Audit
				.createdAt(claim.getCreatedAt())

				.build();
	}

}
