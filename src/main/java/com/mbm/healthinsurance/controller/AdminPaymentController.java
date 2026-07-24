package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.RefundPaymentRequest;
import com.mbm.healthinsurance.dto.response.AdminPaymentDetailsResponse;
import com.mbm.healthinsurance.dto.response.AdminPaymentResponse;
import com.mbm.healthinsurance.dto.response.RefundPaymentResponse;
import com.mbm.healthinsurance.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<AdminPaymentResponse>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments());
    }
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<AdminPaymentDetailsResponse> getPaymentDetails(
            @PathVariable Long paymentId) {

        AdminPaymentDetailsResponse response =
                paymentService.getPaymentDetails(paymentId);

        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{paymentId}/refund")
	public ResponseEntity<RefundPaymentResponse> refundPayment(

	        @PathVariable Long paymentId,

	        @Valid
	        @RequestBody RefundPaymentRequest request) {

	    RefundPaymentResponse response =
	            paymentService.refundPayment(
	                    paymentId,
	                    request);

	    return ResponseEntity.ok(response);
	}

}