package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.CreatePolicyRequest;
import com.mbm.healthinsurance.dto.request.RequestDocument;
import com.mbm.healthinsurance.dto.request.UpdatePolicyRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyStatusRequest;
import com.mbm.healthinsurance.dto.response.AdminPendingPolicyResponse;
import com.mbm.healthinsurance.dto.response.PolicyResponse;
import com.mbm.healthinsurance.service.CustomerPolicyService;
import com.mbm.healthinsurance.service.PolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/policies")
@RequiredArgsConstructor
@Validated
public class PolicyController {

    private final PolicyService policyService;
    private final CustomerPolicyService customerPolicyService;

    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(
            @Valid @RequestBody CreatePolicyRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(policyService.createPolicy(request));
    }
    
    
    @GetMapping
    public ResponseEntity<List<PolicyResponse>> getAllPolicies() {

        return ResponseEntity.ok(
                policyService.getAllPolicies());
    }
    
    @GetMapping("/{policyId}")
    public ResponseEntity<PolicyResponse> getPolicyById(
            @PathVariable Long policyId) {

        return ResponseEntity.ok(
                policyService.getPolicyById(policyId));
    }
    
    @PutMapping("/{policyId}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody UpdatePolicyRequest request) {

        return ResponseEntity.ok(
                policyService.updatePolicy(policyId, request));
    }
    
    @PatchMapping("/{policyId}/status")
    public ResponseEntity<PolicyResponse> updatePolicyStatus(
            @PathVariable Long policyId,
            @Valid @RequestBody UpdatePolicyStatusRequest request) {

        return ResponseEntity.ok(
                policyService.updatePolicyStatus(policyId, request));
    }
    

    @DeleteMapping("/{policyId}")
    public ResponseEntity<Void> deletePolicy(
            @PathVariable Long policyId) {

        policyService.deletePolicy(policyId);

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/pending")
    public List<AdminPendingPolicyResponse> getPendingPolicies() {

        return customerPolicyService
                .getPendingPolicies();
    }
    
    @PatchMapping("/{customerPolicyId}/activate")
    public ResponseEntity<String> activatePolicy(

            @PathVariable Long customerPolicyId) {

        customerPolicyService.activatePolicy(customerPolicyId);

        return ResponseEntity.ok(
                "Policy activated successfully");
    }
    
    @PatchMapping("/{customerPolicyId}/request-documents")
    public ResponseEntity<String> requestDocuments(

            @PathVariable Long customerPolicyId,

            @RequestBody RequestDocument request) {


        customerPolicyService.requestDocuments(
                customerPolicyId,
                request);


        return ResponseEntity.ok(
                "Additional documents requested");
    }
    
}