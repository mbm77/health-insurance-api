package com.mbm.healthinsurance.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.healthinsurance.dto.request.CreatePolicyPaymentRuleRequest;
import com.mbm.healthinsurance.dto.response.PolicyPaymentRuleResponse;
import com.mbm.healthinsurance.entity.Policy;
import com.mbm.healthinsurance.entity.PolicyPaymentRule;
import com.mbm.healthinsurance.enums.LateFeeType;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.PolicyPaymentRuleRepository;
import com.mbm.healthinsurance.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyPaymentRuleService {

	private final PolicyPaymentRuleRepository policyPaymentRuleRepository;

	private final PolicyRepository policyRepository;

	@Transactional
	public PolicyPaymentRuleResponse createPaymentRule(
			Long policyId,
			CreatePolicyPaymentRuleRequest request) {

		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Policy not found with id: " + policyId));

		if (policyPaymentRuleRepository.existsByPolicy(policy)) {

			throw new IllegalArgumentException(
					"Payment rule already exists for this policy.");
		}

		validateLateFee(request);

		PolicyPaymentRule paymentRule = PolicyPaymentRule.builder()
				.policy(policy)
				.gracePeriodInDays(request.getGracePeriodInDays())
				.lateFeeType(request.getLateFeeType())
				.lateFeeValue(request.getLateFeeValue())
				.allowPartialPayment(request.getAllowPartialPayment())
				.maxOverdueDays(request.getMaxOverdueDays())
				.autoLapse(request.getAutoLapse())
				.build();

		PolicyPaymentRule savedRule = policyPaymentRuleRepository.save(paymentRule);

		return mapToResponse(savedRule);
	}

	private void validateLateFee(
			CreatePolicyPaymentRuleRequest request) {
		
		if(request.getGracePeriodInDays() > request.getMaxOverdueDays()) {
		    throw new IllegalArgumentException(
		       "Grace period cannot exceed maximum overdue days.");
		}

		if (request.getLateFeeType() == LateFeeType.NONE) {

			if (request.getLateFeeValue() != null) {
				throw new IllegalArgumentException(
						"Late fee value should not be provided for NONE type.");
			}

			return;
		}

		if (request.getLateFeeValue() == null ||
				request.getLateFeeValue().compareTo(BigDecimal.ZERO) <= 0) {

			throw new IllegalArgumentException(
					"Late fee value must be greater than zero.");
		}

		if (request.getLateFeeType() == LateFeeType.PERCENTAGE &&
				request.getLateFeeValue().compareTo(new BigDecimal("100")) > 0) {

			throw new IllegalArgumentException(
					"Percentage late fee cannot be greater than 100.");
		}
	}

	private PolicyPaymentRuleResponse mapToResponse(
			PolicyPaymentRule rule) {

		return PolicyPaymentRuleResponse.builder()
				.ruleId(rule.getRuleId())
				.policyId(rule.getPolicy().getPolicyId())
				.policyName(rule.getPolicy().getPolicyName())
				.gracePeriodInDays(rule.getGracePeriodInDays())
				.lateFeeType(rule.getLateFeeType())
				.lateFeeValue(rule.getLateFeeValue())
				.allowPartialPayment(rule.getAllowPartialPayment())
				.maxOverdueDays(rule.getMaxOverdueDays())
				.autoLapse(rule.getAutoLapse())
				.build();
	}

	public PolicyPaymentRuleResponse getPaymentRule(Long policyId) {

		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Policy not found with id: " + policyId));

		PolicyPaymentRule paymentRule = policyPaymentRuleRepository.findByPolicy(policy)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Payment rule not found for policy id: " + policyId));

		return mapToResponse(paymentRule);
	}

	@Transactional
	public PolicyPaymentRuleResponse updatePaymentRule(
			Long policyId,
			CreatePolicyPaymentRuleRequest request) {

		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Policy not found with id: " + policyId));

		PolicyPaymentRule paymentRule = policyPaymentRuleRepository.findByPolicy(policy)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Payment rule not found for policy id: " + policyId));

		validateLateFee(request);

		paymentRule.setGracePeriodInDays(
				request.getGracePeriodInDays());

		paymentRule.setLateFeeType(
				request.getLateFeeType());

		paymentRule.setLateFeeValue(
				request.getLateFeeValue());

		paymentRule.setAllowPartialPayment(
				request.getAllowPartialPayment());

		paymentRule.setMaxOverdueDays(
				request.getMaxOverdueDays());

		paymentRule.setAutoLapse(
				request.getAutoLapse());

		PolicyPaymentRule updatedRule = policyPaymentRuleRepository.save(paymentRule);

		return mapToResponse(updatedRule);
	}
}