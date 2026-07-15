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

import com.mbm.healthinsurance.dto.request.CreateCompanyRequest;
import com.mbm.healthinsurance.dto.request.UpdateCompanyRequest;
import com.mbm.healthinsurance.dto.request.UpdateCompanyStatusRequest;
import com.mbm.healthinsurance.dto.response.CompanyResponse;
import com.mbm.healthinsurance.service.InsuranceCompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/companies")
@RequiredArgsConstructor
@Validated
public class AdminCompanyController {

    private final InsuranceCompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @Valid @RequestBody CreateCompanyRequest request) {

        CompanyResponse response = companyService.createCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {

        return ResponseEntity.ok(
                companyService.getAllCompanies());
    }
    
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                companyService.getCompanyById(companyId));
    }
    
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long companyId,
            @Valid @RequestBody UpdateCompanyRequest request) {

        return ResponseEntity.ok(
                companyService.updateCompany(companyId, request));
    }
    
    @PatchMapping("/{companyId}/status")
    public ResponseEntity<CompanyResponse> updateCompanyStatus(
            @PathVariable Long companyId,
            @Valid @RequestBody UpdateCompanyStatusRequest request) {

        return ResponseEntity.ok(
                companyService.updateCompanyStatus(companyId, request));
    }
    
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {

        companyService.deleteCompany(companyId);

        return ResponseEntity.noContent().build();
    }
}