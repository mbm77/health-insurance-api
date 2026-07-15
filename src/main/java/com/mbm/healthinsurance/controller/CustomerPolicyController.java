package com.mbm.healthinsurance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.response.PolicyDetailsResponse;
import com.mbm.healthinsurance.service.PolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers/policies")
@RequiredArgsConstructor
@Validated
public class CustomerPolicyController {

    private final PolicyService policyService;

   
    @GetMapping("/{policyId}")
    public ResponseEntity<PolicyDetailsResponse> getPolicyDetails(
            @PathVariable Long policyId) {

        return ResponseEntity.ok(
                policyService.getPolicyDetails(policyId));
    }
   
    
   
    
    
}
