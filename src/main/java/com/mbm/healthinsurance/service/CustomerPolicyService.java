package com.mbm.healthinsurance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.healthinsurance.dto.request.PurchasePolicyRequest;
import com.mbm.healthinsurance.dto.request.RequestDocument;
import com.mbm.healthinsurance.dto.response.AdminPendingPolicyResponse;
import com.mbm.healthinsurance.dto.response.CustomerPolicyPurchaseResponse;
import com.mbm.healthinsurance.dto.response.PolicyCoverageResponse;
import com.mbm.healthinsurance.dto.response.PolicyExclusionResponse;
import com.mbm.healthinsurance.dto.response.PolicyRenewalHistoryResponse;
import com.mbm.healthinsurance.dto.response.PolicyRenewalResponse;
import com.mbm.healthinsurance.dto.response.PurchasedPolicyResponse;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.InsuranceCompany;
import com.mbm.healthinsurance.entity.InsurancePlan;
import com.mbm.healthinsurance.entity.Policy;
import com.mbm.healthinsurance.entity.PolicyCoverage;
import com.mbm.healthinsurance.entity.PolicyDocumentRequest;
import com.mbm.healthinsurance.entity.PolicyExclusion;
import com.mbm.healthinsurance.entity.PolicyRenewal;
import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.enums.DocumentRequestStatus;
import com.mbm.healthinsurance.enums.NotificationType;
import com.mbm.healthinsurance.enums.PolicyStatus;
import com.mbm.healthinsurance.enums.RenewalStatus;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.PolicyCoverageRepository;
import com.mbm.healthinsurance.repository.PolicyDocumentRequestRepository;
import com.mbm.healthinsurance.repository.PolicyExclusionRepository;
import com.mbm.healthinsurance.repository.PolicyRenewalRepository;
import com.mbm.healthinsurance.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerPolicyService {
	private final CustomerRepository customerRepository;
	private final PolicyRepository policyRepository;
	private final CustomerPolicyRepository customerPolicyRepository;
	private final PolicyCoverageRepository policyCoverageRepository;
	private final PolicyExclusionRepository policyExclusionRepository;
	private final PolicyRenewalRepository policyRenewalRepository;
	private final PolicyDocumentRequestRepository policyDocumentRequestRepository;

	private final PremiumPaymentScheduleService premiumPaymentScheduleService;

	private final NotificationService notificationService;

	@Transactional
	public CustomerPolicyPurchaseResponse purchasePolicy(
			Authentication authentication,
			PurchasePolicyRequest request) {

		// customer found
		Customer customer = customerRepository
				.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		// policy found
		Policy policy = policyRepository.findById(request.getPolicyId())
				.orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

		// check policy status active or inactive
		if (policy.getStatus() != PolicyStatus.ACTIVE) {
			throw new BadRequestException("Policy is not active");
		}

		boolean isCustomerPolicyExists = customerPolicyRepository.existsByCustomerAndPolicy(customer, policy);

		if (isCustomerPolicyExists) {
			throw new BadRequestException(
					"Customer has already purchased this policy");
		}

		CustomerPolicy customerPolicy = new CustomerPolicy();

		customerPolicy.setCustomer(customer);
		customerPolicy.setPolicy(policy);

		customerPolicy.setPurchaseDate(LocalDate.now());

		customerPolicy.setStartDate(LocalDate.now());

		customerPolicy.setExpiryDate(
				customerPolicy.getStartDate()
						.plusMonths(
								policy.getInsurancePlan()
										.getPolicyTermInMonths()));

		customerPolicy.setPremiumFrequency(
				request.getPremiumFrequency());

		// Snapshot values from plan
		customerPolicy.setPremiumAmount(
				policy.getInsurancePlan().getPremiumAmount());

		customerPolicy.setCoverageAmount(
				policy.getInsurancePlan().getCoverageAmount());

		if (policy.getPaymentRule() == null) {
			throw new BadRequestException(
					"Payment rule not configured for this policy");
		}

		// Snapshot payment rule values
		if (policy.getPaymentRule() != null) {

			customerPolicy.setGracePeriodInDays(
					policy.getPaymentRule()
							.getGracePeriodInDays());

			customerPolicy.setLateFeeType(
					policy.getPaymentRule()
							.getLateFeeType());

			customerPolicy.setLateFeeValue(
					policy.getPaymentRule()
							.getLateFeeValue());
		}

		customerPolicy.setStatus(
				CustomerPolicyStatus.PAYMENT_PENDING);

		customerPolicy.setPolicyNumber(
				"POL-" + System.currentTimeMillis());

		CustomerPolicy savedCustomerPolicy = customerPolicyRepository.save(customerPolicy);

		// Generate premium payment schedules
		premiumPaymentScheduleService
				.generatePaymentSchedules(savedCustomerPolicy);

		// notification
		notificationService.createNotification(

				customer,

				"Policy Purchased",

				"Your policy "
						+ savedCustomerPolicy.getPolicyNumber()
						+ " purchase request has been created. Please complete payment to activate your policy.",

				NotificationType.POLICY_PURCHASE,

				"POLICY",

				savedCustomerPolicy.getCustomerPolicyId()

		);

		return CustomerPolicyPurchaseResponse.builder()

				.customerPolicyId(
						savedCustomerPolicy.getCustomerPolicyId())

				.policyNumber(
						savedCustomerPolicy.getPolicyNumber())

				.companyName(
						policy.getInsurancePlan()
								.getInsuranceCompany()
								.getCompanyName())

				.planName(
						policy.getInsurancePlan()
								.getPlanName())

				.policyName(
						policy.getPolicyName())

				.purchaseDate(
						savedCustomerPolicy.getPurchaseDate())

				.startDate(
						savedCustomerPolicy.getStartDate())

				.expiryDate(
						savedCustomerPolicy.getExpiryDate())

				.premiumAmount(
						savedCustomerPolicy.getPremiumAmount())

				.coverageAmount(
						savedCustomerPolicy.getCoverageAmount())

				.premiumFrequency(
						savedCustomerPolicy.getPremiumFrequency())

				.status(
						savedCustomerPolicy.getStatus())

				.build();
	}

	public List<CustomerPolicyPurchaseResponse> getMyPolicies(
			Authentication authentication) {

		// 1. Get logged-in customer username/email from JWT
		String username = authentication.getName();

		// 2. Find customer
		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		// 3. Fetch policies purchased by customer
		List<CustomerPolicy> policies = customerPolicyRepository.findByCustomerOrderByPurchaseDateDesc(customer);

		// 4. Convert Entity to Response DTO
		return policies.stream()
				.map(this::mapToResponse)
				.toList();
	}

	private CustomerPolicyPurchaseResponse mapToResponse(
			CustomerPolicy customerPolicy) {

		CustomerPolicyPurchaseResponse response = new CustomerPolicyPurchaseResponse();

		response.setCustomerPolicyId(customerPolicy.getCustomerPolicyId());
		response.setPolicyNumber(customerPolicy.getPolicyNumber());
		response.setCompanyName(customerPolicy.getPolicy().getInsurancePlan().getInsuranceCompany().getCompanyName());
		response.setPlanName(customerPolicy.getPolicy().getInsurancePlan().getPlanName());
		response.setPolicyName(customerPolicy.getPolicy().getPolicyName());
		response.setPremiumAmount(customerPolicy.getPremiumAmount());
		response.setCoverageAmount(customerPolicy.getCoverageAmount());
		response.setPurchaseDate(customerPolicy.getPurchaseDate());
		response.setStartDate(customerPolicy.getStartDate());
		response.setExpiryDate(customerPolicy.getExpiryDate());
		response.setPremiumFrequency(customerPolicy.getPremiumFrequency());
		response.setStatus(customerPolicy.getStatus());

		return response;
	}

	public PurchasedPolicyResponse getPurchasedPolicy(Authentication authentication, Long customerPolicyId) {

		// Get logged-in customer
		String username = authentication.getName();

		Customer customer = customerRepository.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found"));

		// Get customer's purchased policy
		CustomerPolicy customerPolicy = customerPolicyRepository
				.findByCustomerPolicyIdAndCustomer(
						customerPolicyId,
						customer)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Policy not found"));

		// 3. Convert entity to response DTO
		return mapToCustomerPolicyResponse(customerPolicy);
	}

	private PurchasedPolicyResponse mapToCustomerPolicyResponse(
			CustomerPolicy customerPolicy) {

		PurchasedPolicyResponse response = new PurchasedPolicyResponse();

		/*
		 * Customer Policy Details
		 */
		response.setCustomerPolicyId(customerPolicy.getCustomerPolicyId());
		response.setPolicyCode(customerPolicy.getPolicy().getPolicyCode());
		response.setPremiumFrequency(customerPolicy.getPremiumFrequency());
		response.setPurchaseDate(
				customerPolicy.getPurchaseDate());

		response.setStartDate(
				customerPolicy.getStartDate());

		response.setExpiryDate(
				customerPolicy.getExpiryDate());

		response.setPaidPremiumAmount(
				customerPolicy.getPremiumAmount());

		response.setCustomerPolicyStatus(
				customerPolicy.getStatus());

		/*
		 * Policy Details
		 */
		Policy policy = customerPolicy.getPolicy();

		response.setPolicyId(
				policy.getPolicyId());

		response.setPolicyName(
				policy.getPolicyName());

		response.setPolicyCode(
				policy.getPolicyCode());

		response.setPolicyType(
				policy.getPolicyType());

		response.setMinAge(
				policy.getMinAge());

		response.setMaxAge(
				policy.getMaxAge());

		/*
		 * Plan Details
		 */
		InsurancePlan plan = policy.getInsurancePlan();

		response.setPlanId(
				plan.getPlanId());

		response.setPlanName(
				plan.getPlanName());

		response.setPlanDescription(
				plan.getDescription());

		response.setCoverageAmount(
				plan.getCoverageAmount());

		response.setPremiumAmount(
				plan.getPremiumAmount());

		response.setPolicyTermInMonths(
				plan.getPolicyTermInMonths());

		response.setWaitingPeriodInDays(
				plan.getWaitingPeriodInDays());

		/*
		 * Company Details
		 */
		InsuranceCompany company = plan.getInsuranceCompany();

		response.setCompanyId(
				company.getCompanyId());

		response.setCompanyName(
				company.getCompanyName());

		/*
		 * Coverages
		 */

		List<PolicyCoverage> coverages = policyCoverageRepository
				.findByPolicyPolicyId(customerPolicy.getPolicy().getPolicyId());

		response.setCoverages(
				coverages.stream()
						.map(this::mapCoverage)
						.toList());

		/*
		 * Exclusions
		 */
		List<PolicyExclusion> exclusions = policyExclusionRepository
				.findByPolicyPolicyId(customerPolicy.getPolicy().getPolicyId());
		response.setExclusions(
				exclusions.stream()
						.map(this::mapExclusion)
						.toList());

		return response;
	}

	private PolicyExclusionResponse mapExclusion(PolicyExclusion exclusion) {
		PolicyExclusionResponse response = new PolicyExclusionResponse();
		response.setExclusionId(exclusion.getExclusionId());
		response.setExclusionName(exclusion.getExclusionName());
		response.setStatus(exclusion.getStatus());
		response.setDescription(exclusion.getDescription());
		response.setPolicyId(exclusion.getPolicy().getPolicyId());
		response.setPolicyName(exclusion.getPolicy().getPolicyName());
		return response;
	}

	private PolicyCoverageResponse mapCoverage(PolicyCoverage coverage) {
		PolicyCoverageResponse response = new PolicyCoverageResponse();
		response.setCoverageAmount(coverage.getCoverageAmount());
		response.setCoverageId(coverage.getCoverageId());
		response.setCoverageName(coverage.getCoverageName());
		response.setDescription(coverage.getDescription());
		response.setStatus(coverage.getStatus());
		response.setPolicyName(coverage.getPolicy().getPolicyName());
		response.setPolicyId(coverage.getPolicy().getPolicyId());
		return response;
	}

	public PolicyRenewalResponse renewPolicy(
			Authentication authentication,
			Long customerPolicyId) {

		// Get logged-in customer
		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// Get purchased policy
		CustomerPolicy customerPolicy = customerPolicyRepository
				.findByCustomerPolicyIdAndCustomer(
						customerPolicyId,
						customer)
				.orElseThrow(() -> new RuntimeException("Purchased policy not found"));

		if (customerPolicy.getStatus() != CustomerPolicyStatus.ACTIVE) {
			throw new RuntimeException("Only active policies can be renewed.");
		}

		LocalDate today = LocalDate.now();

		LocalDate renewalWindowStart = customerPolicy.getExpiryDate().minusDays(30);

		if (today.isBefore(renewalWindowStart)) {
			throw new RuntimeException(
					"Policy can be renewed only within 30 days before expiry.");
		}

		if (today.isAfter(customerPolicy.getExpiryDate())) {
			throw new RuntimeException(
					"Policy has expired and cannot be renewed.");
		}

		// Get latest renewal
		Optional<PolicyRenewal> latestRenewal = policyRenewalRepository
				.findTopByCustomerPolicyOrderByRenewalNumberDesc(
						customerPolicy);

		int renewalNumber = latestRenewal
				.map(renewal -> renewal.getRenewalNumber() + 1)
				.orElse(1);

		LocalDate oldStartDate = customerPolicy.getStartDate();
		LocalDate oldExpiryDate = customerPolicy.getExpiryDate();

		LocalDate newStartDate = oldExpiryDate.plusDays(1);

		Integer policyTermInMonths = customerPolicy.getPolicy()
				.getInsurancePlan()
				.getPolicyTermInMonths();

		LocalDate newExpiryDate = newStartDate
				.plusMonths(policyTermInMonths)
				.minusDays(1);

		// Create renewal record
		PolicyRenewal policyRenewal = PolicyRenewal.builder()
				.customerPolicy(customerPolicy)
				.renewalNumber(renewalNumber)
				.renewalDate(LocalDate.now())
				.oldStartDate(oldStartDate)
				.oldExpiryDate(oldExpiryDate)
				.newStartDate(newStartDate)
				.newExpiryDate(newExpiryDate)
				.premiumAmount(customerPolicy.getPremiumAmount())
				.status(RenewalStatus.SUCCESS)
				.build();

		policyRenewalRepository.save(policyRenewal);

		// Update customer policy
		customerPolicy.setStartDate(newStartDate);
		customerPolicy.setExpiryDate(newExpiryDate);

		customerPolicyRepository.save(customerPolicy);

		// notification
		notificationService.createNotification(
				customer,
				"Policy Renewed",
				"Your policy " + customerPolicy.getPolicyNumber() + " has been renewed successfully.",
				NotificationType.POLICY_RENEWAL,
				"POLICY",
				customerPolicy.getCustomerPolicyId());

		return mapToRenewalResponse(
				policyRenewal,
				"Policy renewed successfully.");
	}

	private PolicyRenewalResponse mapToRenewalResponse(
			PolicyRenewal policyRenewal,
			String message) {

		return PolicyRenewalResponse.builder()
				.renewalId(policyRenewal.getRenewalId())
				.customerPolicyId(
						policyRenewal.getCustomerPolicy()
								.getCustomerPolicyId())
				.renewalNumber(policyRenewal.getRenewalNumber())
				.renewalDate(policyRenewal.getRenewalDate())
				.newStartDate(policyRenewal.getNewStartDate())
				.newExpiryDate(policyRenewal.getNewExpiryDate())
				.premiumAmount(policyRenewal.getPremiumAmount())
				.status(policyRenewal.getStatus())
				.message("Policy renewed successfully.")
				.build();
	}

	public List<PolicyRenewalHistoryResponse> getRenewalHistory(
			Authentication authentication,
			Long customerPolicyId) {

		// Get logged-in username
		String username = authentication.getName();

		// Get customer
		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// Get purchased policy
		CustomerPolicy customerPolicy = customerPolicyRepository
				.findByCustomerPolicyIdAndCustomer(
						customerPolicyId,
						customer)
				.orElseThrow(() -> new RuntimeException("Purchased policy not found"));

		// Get renewal history
		List<PolicyRenewal> policyRenewals = policyRenewalRepository
				.findByCustomerPolicyOrderByRenewalNumberDesc(
						customerPolicy);

		// Map to response
		return policyRenewals.stream()
				.map(this::mapToRenewalHistoryResponse)
				.toList();
	}

	private PolicyRenewalHistoryResponse mapToRenewalHistoryResponse(
			PolicyRenewal policyRenewal) {

		return PolicyRenewalHistoryResponse.builder()
				.renewalId(policyRenewal.getRenewalId())
				.renewalNumber(policyRenewal.getRenewalNumber())
				.renewalDate(policyRenewal.getRenewalDate())
				.oldStartDate(policyRenewal.getOldStartDate())
				.oldExpiryDate(policyRenewal.getOldExpiryDate())
				.newStartDate(policyRenewal.getNewStartDate())
				.newExpiryDate(policyRenewal.getNewExpiryDate())
				.premiumAmount(policyRenewal.getPremiumAmount())
				.status(policyRenewal.getStatus())
				.build();
	}

	public List<AdminPendingPolicyResponse> getPendingPolicies() {

		List<CustomerPolicy> policies = customerPolicyRepository
				.findByStatus(
						CustomerPolicyStatus.PAYMENT_PENDING);

		return policies.stream()
				.map(this::mapToAdminPendingPolicyResponse)
				.toList();
	}

	private AdminPendingPolicyResponse mapToAdminPendingPolicyResponse(
			CustomerPolicy customerPolicy) {

		AdminPendingPolicyResponse response = new AdminPendingPolicyResponse();

		response.setCustomerPolicyId(
				customerPolicy.getCustomerPolicyId());

		response.setCustomerId(
				customerPolicy.getCustomer()
						.getCustomerId());

		response.setCustomerName(
				customerPolicy.getCustomer()
						.getFirstName()
						+ " "
						+ customerPolicy.getCustomer()
								.getLastName());

		response.setPolicyNumber(
				customerPolicy.getPolicyNumber());

		response.setPolicyName(
				customerPolicy.getPolicy()
						.getPolicyName());

		response.setPremiumAmount(
				customerPolicy.getPremiumAmount());

		response.setStatus(
				customerPolicy.getStatus().name());

		response.setStartDate(
				customerPolicy.getStartDate());

		return response;
	}

	@Transactional
	public void activatePolicy(Long customerPolicyId) {

		CustomerPolicy customerPolicy = customerPolicyRepository
				.findById(customerPolicyId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer policy not found"));

		if (customerPolicy.getStatus() != CustomerPolicyStatus.PAYMENT_PENDING) {

			throw new BadRequestException(
					"Only pending policies can be activated");
		}

		customerPolicy.setStatus(
				CustomerPolicyStatus.ACTIVE);

		customerPolicyRepository.save(customerPolicy);
	}

	@Transactional
	public void requestDocuments(
			Long customerPolicyId,
			RequestDocument request) {

		CustomerPolicy customerPolicy = customerPolicyRepository
				.findById(customerPolicyId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer policy not found"));

		if (customerPolicy.getStatus() != CustomerPolicyStatus.PAYMENT_PENDING) {

			throw new BadRequestException(
					"Only pending policies can request documents");
		}

		PolicyDocumentRequest documentRequest = new PolicyDocumentRequest();

		documentRequest.setCustomerPolicy(
				customerPolicy);

		documentRequest.setRemarks(
				request.getRemarks());

		documentRequest.setStatus(
				DocumentRequestStatus.PENDING);

		documentRequest.setRequestedAt(
				LocalDateTime.now());

		policyDocumentRequestRepository
				.save(documentRequest);
	}

}
