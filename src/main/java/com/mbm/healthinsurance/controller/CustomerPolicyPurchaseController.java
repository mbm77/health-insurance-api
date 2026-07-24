package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.PurchasePolicyRequest;
import com.mbm.healthinsurance.dto.response.ApiResponse;
import com.mbm.healthinsurance.dto.response.CustomerPolicyPurchaseResponse;
import com.mbm.healthinsurance.dto.response.PolicyRenewalHistoryResponse;
import com.mbm.healthinsurance.dto.response.PolicyRenewalResponse;
import com.mbm.healthinsurance.dto.response.PurchasedPolicyResponse;
import com.mbm.healthinsurance.service.CustomerPolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
@Validated
public class CustomerPolicyPurchaseController {
	private final CustomerPolicyService customerPolicyService;

	@PostMapping("/policies/purchase")
	public ResponseEntity<ApiResponse<CustomerPolicyPurchaseResponse>> purchasePolicy(
			Authentication authentication,
			@Valid @RequestBody PurchasePolicyRequest request) {

		CustomerPolicyPurchaseResponse customerPolicyResponse = customerPolicyService
				.purchasePolicy(authentication, request);
		ApiResponse<CustomerPolicyPurchaseResponse> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("policy purchased successfully.");
		response.setData(customerPolicyResponse);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	@GetMapping("/purchased-policies")
	public ResponseEntity<ApiResponse<List<CustomerPolicyPurchaseResponse>>> getMyPolicies(
			Authentication authentication) {

		List<CustomerPolicyPurchaseResponse> customerPolicyResponse = customerPolicyService
				.getMyPolicies(authentication);
		ApiResponse<List<CustomerPolicyPurchaseResponse>> response = new ApiResponse<>();

		response.setSuccess(true);
		if (!customerPolicyResponse.isEmpty()) {

			response.setMessage("Customer policies retrieved successfully.");
			response.setData(customerPolicyResponse);

		} else {
			response.setMessage("No policies found.");
			response.setData(customerPolicyResponse);
		}

		return ResponseEntity.ok(response);

	}

	@GetMapping("/purchased-policies/{customerPolicyId}")
	public ResponseEntity<ApiResponse<PurchasedPolicyResponse>> getMyPolicy(
			Authentication authentication, @PathVariable Long customerPolicyId) {

		PurchasedPolicyResponse purchasedPolicyResponse = customerPolicyService
				.getPurchasedPolicy(authentication, customerPolicyId);

		ApiResponse<PurchasedPolicyResponse> response = new ApiResponse<>();

		response.setSuccess(true);
		if (purchasedPolicyResponse != null) {
			response.setMessage("Customer purchased policy retrieved successfully.");
			response.setData(purchasedPolicyResponse);
		} else {
			response.setMessage("No policy found.");
			response.setData(purchasedPolicyResponse);
		}

		return ResponseEntity.ok(response);

	}

	@PostMapping("/purchased-policies/{customerPolicyId}/renew")
	public ResponseEntity<PolicyRenewalResponse> renewPolicy(
			Authentication authentication,
			@PathVariable Long customerPolicyId) {

		return ResponseEntity.ok(
				customerPolicyService.renewPolicy(
						authentication,
						customerPolicyId));
	}

	@GetMapping("/purchased-policies/{customerPolicyId}/renewals")
	public ResponseEntity<List<PolicyRenewalHistoryResponse>> getRenewalHistory(
			Authentication authentication,
			@PathVariable Long customerPolicyId) {

		return ResponseEntity.ok(
				customerPolicyService.getRenewalHistory(
						authentication,
						customerPolicyId));
	}

}
