package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.response.CompanyResponse;
import com.mbm.healthinsurance.dto.response.InsurancePlanResponse;
import com.mbm.healthinsurance.service.InsuranceCompanyService;
import com.mbm.healthinsurance.service.InsurancePlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final InsuranceCompanyService companyService;
    private final InsurancePlanService insurancePlanService;

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllActiveCompanies() {

        return ResponseEntity.ok(
                companyService.getAllActiveCompanies());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                companyService.getCompanyById(companyId));
    }
    
    @GetMapping("/{companyId}/plans")
    public ResponseEntity<List<InsurancePlanResponse>> getPlansByCompany(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                insurancePlanService.getPlansByCompany(companyId));
    }
}