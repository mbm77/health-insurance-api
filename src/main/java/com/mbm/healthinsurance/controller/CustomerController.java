package com.mbm.healthinsurance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.ChangePasswordRequest;
import com.mbm.healthinsurance.dto.request.UpdateCustomerProfileRequest;
import com.mbm.healthinsurance.dto.response.AdminCustomerResponse;
import com.mbm.healthinsurance.dto.response.MessageResponse;
import com.mbm.healthinsurance.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    
    
/*
    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileResponse> getProfile(
            Authentication authentication) {

        return ResponseEntity.ok(
                customerService.getProfile(authentication.getName()));
    }
    
    
    @PutMapping("/profile")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateCustomerProfileRequest request) {

        return ResponseEntity.ok(
                customerService.updateProfile(
                        authentication.getName(),
                        request));
    }
    
    */
    
    @GetMapping("/profile")
    public ResponseEntity<AdminCustomerResponse> getProfile() {

        return ResponseEntity.ok(customerService.getProfile());
    }
    
    @PutMapping("/profile")
    public ResponseEntity<AdminCustomerResponse> updateProfile(
            @Valid @RequestBody UpdateCustomerProfileRequest request) {

        return ResponseEntity.ok(customerService.updateProfile(request));
    }
    
    @PatchMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(customerService.changePassword(request));
    }
}