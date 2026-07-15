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

import com.mbm.healthinsurance.dto.request.CreatePolicyCoverageRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyCoverageRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyCoverageStatusRequest;
import com.mbm.healthinsurance.dto.response.PolicyCoverageResponse;
import com.mbm.healthinsurance.service.PolicyCoverageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/policy-coverages")
@RequiredArgsConstructor
@Validated
public class PolicyCoverageController {

    private final PolicyCoverageService policyCoverageService;

    @PostMapping
    public ResponseEntity<PolicyCoverageResponse> createCoverage(
            @Valid @RequestBody CreatePolicyCoverageRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(policyCoverageService.createCoverage(request));
    }
    
    
    @GetMapping
    public ResponseEntity<List<PolicyCoverageResponse>> getAllCoverages() {

        return ResponseEntity.ok(
                policyCoverageService.getAllCoverages());
    }
    
    @GetMapping("/{coverageId}")
    public ResponseEntity<PolicyCoverageResponse> getCoverageById(
            @PathVariable Long coverageId) {

        return ResponseEntity.ok(
                policyCoverageService.getCoverageById(coverageId));
    }
    
   

    @PutMapping("/{coverageId}")
    public ResponseEntity<PolicyCoverageResponse> updateCoverage(
            @PathVariable Long coverageId,
            @Valid @RequestBody UpdatePolicyCoverageRequest request) {

        return ResponseEntity.ok(
                policyCoverageService.updateCoverage(
                        coverageId,
                        request));
    }
    
    @PatchMapping("/{coverageId}/status")
    public ResponseEntity<PolicyCoverageResponse> updateCoverageStatus(
            @PathVariable Long coverageId,
            @Valid @RequestBody UpdatePolicyCoverageStatusRequest request) {

        return ResponseEntity.ok(
                policyCoverageService.updateCoverageStatus(coverageId, request));
    }

    

    @DeleteMapping("/{coverageId}")
    public ResponseEntity<Void> deleteCoverage(
            @PathVariable Long coverageId) {

        policyCoverageService.deleteCoverage(coverageId);

        return ResponseEntity.noContent().build();
    } 
    
   
}