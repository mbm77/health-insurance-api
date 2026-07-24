package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.ApproveClaimRequest;
import com.mbm.healthinsurance.dto.request.ClaimPaymentRequest;
import com.mbm.healthinsurance.dto.request.RejectClaimRequest;
import com.mbm.healthinsurance.dto.response.AdminClaimDetailsResponse;
import com.mbm.healthinsurance.dto.response.AdminClaimsResponse;
import com.mbm.healthinsurance.dto.response.ApiResponse;
import com.mbm.healthinsurance.dto.response.ApproveClaimResponse;
import com.mbm.healthinsurance.dto.response.ClaimPaymentResponse;
import com.mbm.healthinsurance.dto.response.RejectClaimResponse;
import com.mbm.healthinsurance.service.ClaimService;
import com.mbm.healthinsurance.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminClaimController {

	private final ClaimService claimService;
	private final PaymentService paymentService;

	@GetMapping("/claims/{claimId}")
	public ResponseEntity<AdminClaimDetailsResponse> getClaimDetails(
			@PathVariable Long claimId) {

		return ResponseEntity.ok(
				claimService.getClaimDetails(claimId));
	}

	@GetMapping("/claims")
	public ResponseEntity<List<AdminClaimsResponse>> getAllClaims() {

		return ResponseEntity.ok(
				claimService.getAllClaims());
	}

	@PatchMapping("/claims/{claimId}/approve")
	public ResponseEntity<ApproveClaimResponse> approveClaim(
			@PathVariable Long claimId,
			@Valid @RequestBody ApproveClaimRequest request) {

		return ResponseEntity.ok(
				claimService.approveClaim(
						claimId,
						request));
	}

	@PatchMapping("/claims/{claimId}/reject")
	public ResponseEntity<RejectClaimResponse> rejectClaim(
			@PathVariable Long claimId,
			@Valid @RequestBody RejectClaimRequest request) {

		return ResponseEntity.ok(
				claimService.rejectClaim(
						claimId,
						request));
	}

	@PostMapping("claims/{claimId}/payment")
	public ResponseEntity<ClaimPaymentResponse> processClaimPayment(
			@PathVariable Long claimId,
			@Valid @RequestBody ClaimPaymentRequest request) {

		return ResponseEntity.ok(
				paymentService.processClaimPayment(claimId, request));
	}

	
	@GetMapping("/claims/{claimId}/payment")
	public ResponseEntity<ClaimPaymentResponse> getClaimPayment(
	        @PathVariable Long claimId) {

	    return ResponseEntity.ok(paymentService.getClaimPayment(claimId));
	}

}
