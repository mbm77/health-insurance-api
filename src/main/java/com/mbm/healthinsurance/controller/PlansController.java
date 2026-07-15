package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.response.InsurancePlanResponse;
import com.mbm.healthinsurance.dto.response.PolicyResponse;
import com.mbm.healthinsurance.service.InsurancePlanService;
import com.mbm.healthinsurance.service.PolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers/plans")
@RequiredArgsConstructor
public class PlansController {

   private final PolicyService policyService;
   private final InsurancePlanService planService;

    @GetMapping("/{planId}/policies")
    public ResponseEntity<List<PolicyResponse>> getPoliciesByPlan(
            @PathVariable Long planId ) {

        return ResponseEntity.ok(
                policyService.getPoliciesByPlan(planId));
    }
    
    @GetMapping("/plans/{planId}")
    public ResponseEntity<InsurancePlanResponse> getPlanById(
            @PathVariable Long planId) {

        return ResponseEntity.ok(
                planService.getPlanByIdAndStatus(planId));
    }
}