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

import com.mbm.healthinsurance.dto.request.CreatePolicyExclusionRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyExclusionRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyExclusionStatusRequest;
import com.mbm.healthinsurance.dto.response.PolicyExclusionResponse;
import com.mbm.healthinsurance.service.PolicyExclusionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/policy-exclusions")
@RequiredArgsConstructor
@Validated
public class PolicyExclusionController {

    private final PolicyExclusionService policyExclusionService;

    @PostMapping
    public ResponseEntity<PolicyExclusionResponse> createExclusion(
            @Valid @RequestBody CreatePolicyExclusionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(policyExclusionService.createExclusion(request));
    }

    @GetMapping
    public ResponseEntity<List<PolicyExclusionResponse>> getAllExclusions() {

        return ResponseEntity.ok(
                policyExclusionService.getAllExclusions());
    }
    
    

    @GetMapping("/{exclusionId}")
    public ResponseEntity<PolicyExclusionResponse> getExclusionById(
            @PathVariable Long exclusionId) {

        return ResponseEntity.ok(
                policyExclusionService.getExclusionById(exclusionId));
    }
    
    

    @PutMapping("/{exclusionId}")
    public ResponseEntity<PolicyExclusionResponse> updateExclusion(
            @PathVariable Long exclusionId,
            @Valid @RequestBody UpdatePolicyExclusionRequest request) {

        return ResponseEntity.ok(
                policyExclusionService.updateExclusion(
                        exclusionId,
                        request));
    }

    @PatchMapping("/{exclusionId}/status")
    public ResponseEntity<PolicyExclusionResponse> updateExclusionStatus(
            @PathVariable Long exclusionId,
            @Valid @RequestBody UpdatePolicyExclusionStatusRequest request) {

        return ResponseEntity.ok(
                policyExclusionService.updateExclusionStatus(
                        exclusionId,
                        request));
    }
    
    
    @DeleteMapping("/{exclusionId}")
    public ResponseEntity<Void> deleteExclusion(
            @PathVariable Long exclusionId) {

        policyExclusionService.deleteExclusion(exclusionId);

        return ResponseEntity.noContent().build();
    }
    
}