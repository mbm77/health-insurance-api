package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.CreateInsurancePlanRequest;
import com.mbm.healthinsurance.dto.request.UpdateInsurancePlanRequest;
import com.mbm.healthinsurance.dto.response.InsurancePlanResponse;
import com.mbm.healthinsurance.service.InsurancePlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/plans")
@RequiredArgsConstructor
@Validated
public class AdminPlanController {
/*
    private final InsurancePlanService insurancePlanService;

    @PostMapping
    public ResponseEntity<InsurancePlanResponse> createPlan(
            @Valid @RequestBody CreateInsurancePlanRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(insurancePlanService.createPlan(request));
    }
    
    @GetMapping
    public ResponseEntity<List<InsurancePlanResponse>> getAllPlans() {

        return ResponseEntity.ok(
                insurancePlanService.getAllPlans());
    }
    
    @GetMapping("/{planId}")
    public ResponseEntity<InsurancePlanResponse> getPlanById(
            @PathVariable Long planId) {

        return ResponseEntity.ok(
                insurancePlanService.getPlanById(planId));
    }
    
    @PutMapping("/{planId}")
    public ResponseEntity<InsurancePlanResponse> updatePlan(
            @PathVariable Long planId,
            @Valid @RequestBody UpdateInsurancePlanRequest request) {

        return ResponseEntity.ok(
                insurancePlanService.updatePlan(planId, request));
    }
    
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long planId) {

        insurancePlanService.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }
    */
}