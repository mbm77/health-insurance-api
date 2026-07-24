package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.ClaimRequest;
import com.mbm.healthinsurance.dto.response.ClaimDetailsResponse;
import com.mbm.healthinsurance.dto.response.ClaimPaymentResponse;
import com.mbm.healthinsurance.dto.response.ClaimResponse;
import com.mbm.healthinsurance.dto.response.MyClaimsResponse;
import com.mbm.healthinsurance.service.ClaimService;
import com.mbm.healthinsurance.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class ClaimController {

	private final ClaimService claimService;
	private final PaymentService paymentService;

	@PostMapping("/purchased-policies/{customerPolicyId}/claims")
	public ResponseEntity<ClaimResponse> raiseClaim(
			Authentication authentication,
			@PathVariable Long customerPolicyId,
			@Valid @RequestBody ClaimRequest request) {

		return ResponseEntity.ok(
				claimService.raiseClaim(
						authentication,
						customerPolicyId,
						request));
	}

	@GetMapping("/claims")
	public ResponseEntity<List<MyClaimsResponse>> getMyClaims(
			Authentication authentication) {

		return ResponseEntity.ok(
				claimService.getMyClaims(authentication));

	}

	@GetMapping("/claims/{claimId}")
	public ResponseEntity<ClaimDetailsResponse> getClaimDetails(
			Authentication authentication,
			@PathVariable Long claimId) {

		return ResponseEntity.ok(
				claimService.getClaimDetails(
						authentication,
						claimId));
	}

	@GetMapping("/claims/{claimId}/payment")
	public ResponseEntity<ClaimPaymentResponse> getClaimPayment(
			@PathVariable Long claimId) {

		return ResponseEntity.ok(
				paymentService.getCustomerClaimPayment(claimId));
	}

}