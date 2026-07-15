package com.mbm.healthinsurance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.PurchasePolicyRequest;
import com.mbm.healthinsurance.dto.response.CustomerPolicyPurchaseResponse;
import com.mbm.healthinsurance.service.CustomerPolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
@Validated
public class CustomerPurchasePolicyController {
	private final CustomerPolicyService customerPolicyService;
	
	@PostMapping("/policies/purchase")
	public ResponseEntity<CustomerPolicyPurchaseResponse> purchasePolicy(
	        Authentication authentication,
	        @Valid @RequestBody PurchasePolicyRequest request) {

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(customerPolicyService.purchasePolicy(authentication, request));
	}
	
}
