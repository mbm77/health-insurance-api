package com.mbm.healthinsurance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.CreatePolicyPaymentRuleRequest;
import com.mbm.healthinsurance.dto.response.PolicyPaymentRuleResponse;
import com.mbm.healthinsurance.service.PolicyPaymentRuleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/policies")
@RequiredArgsConstructor
public class AdminPolicyPaymentRuleController {

	private final PolicyPaymentRuleService policyPaymentRuleService;

	@PostMapping("/{policyId}/payment-rule")
	public ResponseEntity<PolicyPaymentRuleResponse> createPaymentRule(
			@PathVariable Long policyId,
			@Valid @RequestBody CreatePolicyPaymentRuleRequest request) {

		PolicyPaymentRuleResponse response = policyPaymentRuleService.createPaymentRule(policyId, request);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}

	@GetMapping("/{policyId}/payment-rule")
	public ResponseEntity<PolicyPaymentRuleResponse> getPaymentRule(
			@PathVariable Long policyId) {

		PolicyPaymentRuleResponse response = policyPaymentRuleService.getPaymentRule(policyId);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{policyId}/payment-rule")
	public ResponseEntity<PolicyPaymentRuleResponse> updatePaymentRule(
			@PathVariable Long policyId,
			@Valid @RequestBody CreatePolicyPaymentRuleRequest request) {

		PolicyPaymentRuleResponse response = policyPaymentRuleService.updatePaymentRule(policyId, request);

		return ResponseEntity.ok(response);
	}

}