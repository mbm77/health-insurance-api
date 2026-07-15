package com.mbm.healthinsurance.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.PurchasePolicyRequest;
import com.mbm.healthinsurance.dto.response.CustomerPolicyPurchaseResponse;
import com.mbm.healthinsurance.enums.PolicyStatus;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.model.Customer;
import com.mbm.healthinsurance.model.Policy;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerPolicyService {
	private final CustomerRepository customerRepository;
	private final PolicyRepository policyRepository;

	public CustomerPolicyPurchaseResponse purchasePolicy(Authentication authentication, PurchasePolicyRequest request) {

		Customer customer = customerRepository.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		System.err.println(customer.getCustomerId());

		Policy policy = policyRepository.findById(request.getPolicyId())
				.orElseThrow(() -> new ResourceNotFoundException("Policy not found"));
		System.err.println(policy.getPolicyName());
		
		if (policy.getStatus() != PolicyStatus.ACTIVE) {
			throw new BadRequestException("policy is not active");
		}
		return null;
	}

}
