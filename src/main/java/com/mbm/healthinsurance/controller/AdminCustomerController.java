package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.UpdateAdminCustomerStatusRequest;
import com.mbm.healthinsurance.dto.request.UpdateCustomerProfileRequest;
import com.mbm.healthinsurance.dto.response.AdminCustomerResponse;
import com.mbm.healthinsurance.service.AdminCustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCustomerController {
	private final AdminCustomerService adminCustomerService;
	@GetMapping("/customers")
	public ResponseEntity<List<AdminCustomerResponse>> getAllCustomers() {

	    return ResponseEntity.ok(
	            adminCustomerService.getAllCustomers());
	}
	
	@GetMapping("/customers/{customerId}")
	public ResponseEntity<AdminCustomerResponse> getCustomerById(
	        @PathVariable Long customerId) {

	    return ResponseEntity.ok(
	            adminCustomerService.getCustomerById(customerId));
	}
	
	@PatchMapping("/customers/{customerId}/status")
	public ResponseEntity<AdminCustomerResponse> updateCustomerStatus(
	        @PathVariable Long customerId,
	        @Valid @RequestBody UpdateAdminCustomerStatusRequest request) {

	    return ResponseEntity.ok(
	            adminCustomerService.updateCustomerStatus(customerId, request));
	}
	
	@PutMapping("/customers/{customerId}")
	public ResponseEntity<AdminCustomerResponse> updateCustomer(
	        @PathVariable Long customerId,
	        @Valid @RequestBody UpdateCustomerProfileRequest request) {

	    return ResponseEntity.ok(
	            adminCustomerService.updateAdminCustomer(customerId, request));
	}
	

}
