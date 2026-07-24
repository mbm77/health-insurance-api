package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.PaymentRequest;
import com.mbm.healthinsurance.dto.request.RefundPaymentRequest;
import com.mbm.healthinsurance.dto.response.PaymentDetailsResponse;
import com.mbm.healthinsurance.dto.response.PaymentHistoryResponse;
import com.mbm.healthinsurance.dto.response.PaymentResponse;
import com.mbm.healthinsurance.dto.response.RefundPaymentResponse;
import com.mbm.healthinsurance.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<PaymentResponse> createPayment(
			Authentication authentication,
			@Valid @RequestBody PaymentRequest request) {

		PaymentResponse response = paymentService.createPayment(
				authentication,
				request);

		return new ResponseEntity<>(
				response,
				HttpStatus.CREATED);
	}

	@PatchMapping("/{paymentId}/success")
	public ResponseEntity<PaymentResponse> successPayment(
			Authentication authentication,
			@PathVariable Long paymentId) {

		PaymentResponse response = paymentService.successPayment(
				authentication,
				paymentId);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{paymentId}/failed")
	public ResponseEntity<PaymentResponse> failedPayment(
			Authentication authentication,
			@PathVariable Long paymentId) {

		PaymentResponse response = paymentService.failedPayment(
				authentication,
				paymentId);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<PaymentHistoryResponse>> getMyPayments(
			Authentication authentication) {

		List<PaymentHistoryResponse> response = paymentService.getMyPayments(authentication);

		return ResponseEntity.ok(response);

	}

	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentDetailsResponse> getPaymentDetails(
			Authentication authentication,
			@PathVariable Long paymentId) {

		return ResponseEntity.ok(
				paymentService.getPaymentDetails(
						authentication,
						paymentId));
	}
	
	

}