package com.mbm.healthinsurance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.CreatePolicyCoverageRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyCoverageRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyCoverageStatusRequest;
import com.mbm.healthinsurance.dto.response.PolicyCoverageResponse;
import com.mbm.healthinsurance.enums.CoverageStatus;
import com.mbm.healthinsurance.enums.PolicyStatus;
import com.mbm.healthinsurance.exception.ResourceAlreadyExistsException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.model.Policy;
import com.mbm.healthinsurance.model.PolicyCoverage;
import com.mbm.healthinsurance.repository.PolicyCoverageRepository;
import com.mbm.healthinsurance.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyCoverageService {
	
	private final PolicyRepository policyRepository;
	private final PolicyCoverageRepository policyCoverageRepository;
	
	
	public PolicyCoverageResponse createCoverage(CreatePolicyCoverageRequest request) {

	    Policy policy = policyRepository.findById(request.getPolicyId())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Policy not found with id: " + request.getPolicyId()));

	    if (policy.getStatus() == PolicyStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Cannot add coverage to an inactive policy.");
	    }

	    if (policyCoverageRepository.existsByPolicyPolicyIdAndCoverageNameIgnoreCase(
	            request.getPolicyId(),
	            request.getCoverageName())) {

	        throw new ResourceAlreadyExistsException(
	                "Coverage name already exists for this policy.");
	    }

	    PolicyCoverage coverage = PolicyCoverage.builder()
	            .policy(policy)
	            .coverageName(request.getCoverageName())
	            .coverageAmount(request.getCoverageAmount())
	            .description(request.getDescription()).status(CoverageStatus.ACTIVE)
	            .build();

	    PolicyCoverage savedCoverage = policyCoverageRepository.save(coverage);

	    return PolicyCoverageResponse.builder()
	            .coverageId(savedCoverage.getCoverageId())
	            .policyId(policy.getPolicyId())
	            .policyName(policy.getPolicyName())
	            .coverageName(savedCoverage.getCoverageName())
	            .coverageAmount(savedCoverage.getCoverageAmount())
	            .description(savedCoverage.getDescription()).status(savedCoverage.getStatus())
	            .build();
	}
	
	/*
	
	public List<PolicyCoverageResponse> getAllCoverages() {

	    List<PolicyCoverage> coverages = policyCoverageRepository.findAll();

	    return coverages.stream()
	            .map(coverage -> PolicyCoverageResponse.builder()
	                    .coverageId(coverage.getCoverageId())
	                    .policyId(coverage.getPolicy().getPolicyId())
	                    .policyName(coverage.getPolicy().getPolicyName())
	                    .coverageName(coverage.getCoverageName())
	                    .coverageAmount(coverage.getCoverageAmount())
	                    .description(coverage.getDescription()).status(coverage.getStatus())
	                    .build())
	            .toList();
	}
	
	*/
	
	
	public List<PolicyCoverageResponse> getAllCoverages() {

	    List<PolicyCoverage> coverages =
	            policyCoverageRepository.findByPolicyStatus(PolicyStatus.ACTIVE);

	    return coverages.stream()
	            .map(coverage -> PolicyCoverageResponse.builder()
	                    .coverageId(coverage.getCoverageId())
	                    .policyId(coverage.getPolicy().getPolicyId())
	                    .policyName(coverage.getPolicy().getPolicyName())
	                    .coverageName(coverage.getCoverageName())
	                    .coverageAmount(coverage.getCoverageAmount())
	                    .description(coverage.getDescription()).status(coverage.getStatus())
	                    .build())
	            .toList();
	}
	
	
	public PolicyCoverageResponse getCoverageById(Long coverageId) {

	    PolicyCoverage coverage = policyCoverageRepository.findById(coverageId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Coverage not found with id: " + coverageId));
/*
	    if (coverage.getStatus() == CoverageStatus.INACTIVE) {
	        throw new ResourceNotFoundException(
	                "Coverage not found with id: " + coverageId);
	    }
	    */

	    return PolicyCoverageResponse.builder()
	            .coverageId(coverage.getCoverageId())
	            .policyId(coverage.getPolicy().getPolicyId())
	            .policyName(coverage.getPolicy().getPolicyName())
	            .coverageName(coverage.getCoverageName())
	            .coverageAmount(coverage.getCoverageAmount())
	            .description(coverage.getDescription())
	            .status(coverage.getStatus())
	            .build();
	}
	
	
	public PolicyCoverageResponse updateCoverage(
	        Long coverageId,
	        UpdatePolicyCoverageRequest request) {

	    PolicyCoverage coverage = policyCoverageRepository.findById(coverageId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Coverage not found with id: " + coverageId));

	    if (coverage.getStatus() == CoverageStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Inactive coverage cannot be updated.");
	    }

	    Policy policy = policyRepository.findById(request.getPolicyId())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Policy not found with id: " + request.getPolicyId()));

	    if (policy.getStatus() == PolicyStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Cannot assign an inactive policy.");
	    }

	    if (policyCoverageRepository
	            .existsByPolicyPolicyIdAndCoverageNameIgnoreCaseAndCoverageIdNot(
	                    request.getPolicyId(),
	                    request.getCoverageName(),
	                    coverageId)) {

	        throw new ResourceAlreadyExistsException(
	                "Coverage name already exists for this policy.");
	    }

	    coverage.setPolicy(policy);
	    coverage.setCoverageName(request.getCoverageName());
	    coverage.setCoverageAmount(request.getCoverageAmount());
	    coverage.setDescription(request.getDescription());

	    PolicyCoverage updatedCoverage = policyCoverageRepository.save(coverage);

	    return PolicyCoverageResponse.builder()
	            .coverageId(updatedCoverage.getCoverageId())
	            .policyId(updatedCoverage.getPolicy().getPolicyId())
	            .policyName(updatedCoverage.getPolicy().getPolicyName())
	            .coverageName(updatedCoverage.getCoverageName())
	            .coverageAmount(updatedCoverage.getCoverageAmount())
	            .description(updatedCoverage.getDescription())
	            .status(updatedCoverage.getStatus())
	            .build();
	}
	
	public PolicyCoverageResponse updateCoverageStatus(
	        Long coverageId,
	        UpdatePolicyCoverageStatusRequest request) {

	    PolicyCoverage coverage = policyCoverageRepository.findById(coverageId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Coverage not found with id: " + coverageId));

	    coverage.setStatus(request.getStatus());

	    PolicyCoverage updatedCoverage = policyCoverageRepository.save(coverage);

	    return PolicyCoverageResponse.builder()
	            .coverageId(updatedCoverage.getCoverageId())
	            .policyId(updatedCoverage.getPolicy().getPolicyId())
	            .policyName(updatedCoverage.getPolicy().getPolicyName())
	            .coverageName(updatedCoverage.getCoverageName())
	            .coverageAmount(updatedCoverage.getCoverageAmount())
	            .description(updatedCoverage.getDescription())
	            .status(updatedCoverage.getStatus())
	            .build();
	}

	public void deleteCoverage(Long coverageId) {

	    PolicyCoverage coverage = policyCoverageRepository.findById(coverageId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Coverage not found with id: " + coverageId));

	    if (coverage.getStatus() == CoverageStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Coverage is already inactive.");
	    }

	    coverage.setStatus(CoverageStatus.INACTIVE);

	    policyCoverageRepository.save(coverage);
	}

}