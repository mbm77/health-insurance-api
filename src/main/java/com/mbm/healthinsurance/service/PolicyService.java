package com.mbm.healthinsurance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.CreatePolicyRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyStatusRequest;
import com.mbm.healthinsurance.dto.response.PolicyCoverageResponse;
import com.mbm.healthinsurance.dto.response.PolicyDetailsResponse;
import com.mbm.healthinsurance.dto.response.PolicyExclusionResponse;
import com.mbm.healthinsurance.dto.response.PolicyResponse;
import com.mbm.healthinsurance.enums.CompanyStatus;
import com.mbm.healthinsurance.enums.CoverageStatus;
import com.mbm.healthinsurance.enums.ExclusionStatus;
import com.mbm.healthinsurance.enums.PlanStatus;
import com.mbm.healthinsurance.enums.PolicyStatus;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceAlreadyExistsException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.model.InsuranceCompany;
import com.mbm.healthinsurance.model.InsurancePlan;
import com.mbm.healthinsurance.model.Policy;
import com.mbm.healthinsurance.model.PolicyCoverage;
import com.mbm.healthinsurance.model.PolicyExclusion;
import com.mbm.healthinsurance.repository.InsurancePlanRepository;
import com.mbm.healthinsurance.repository.PolicyCoverageRepository;
import com.mbm.healthinsurance.repository.PolicyExclusionRepository;
import com.mbm.healthinsurance.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    private final InsurancePlanRepository insurancePlanRepository;
    

    private final PolicyCoverageRepository policyCoverageRepository;

    private final PolicyExclusionRepository policyExclusionRepository;

    public PolicyResponse createPolicy(CreatePolicyRequest request) {

        InsurancePlan insurancePlan = insurancePlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insurance plan not found with id: " + request.getPlanId()));

        if (insurancePlan.getStatus() == PlanStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Cannot create policy for an inactive insurance plan.");
        }

        if (insurancePlan.getInsuranceCompany().getStatus() == CompanyStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Cannot create policy because the insurance company is inactive.");
        }

        if (policyRepository.existsByPolicyCodeIgnoreCase(request.getPolicyCode())) {
            throw new ResourceAlreadyExistsException(
                    "Policy code already exists.");
        }

        if (policyRepository.existsByPolicyNameIgnoreCase(request.getPolicyName())) {
            throw new ResourceAlreadyExistsException(
                    "Policy name already exists.");
        }

        if (request.getMinAge() > request.getMaxAge()) {
            throw new BadRequestException(
                    "Minimum age cannot be greater than maximum age.");
        }

        Policy policy = Policy.builder()
                .insurancePlan(insurancePlan)
                .policyName(request.getPolicyName())
                .policyCode(request.getPolicyCode())
                .policyType(request.getPolicyType())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .status(PolicyStatus.ACTIVE)
                .build();

        Policy savedPolicy = policyRepository.save(policy);

        return PolicyResponse.builder()
                .policyId(savedPolicy.getPolicyId())
                .companyId(insurancePlan.getInsuranceCompany().getCompanyId())
                .companyName(insurancePlan.getInsuranceCompany().getCompanyName())
                .planId(insurancePlan.getPlanId())
                .planName(insurancePlan.getPlanName())
                .policyName(savedPolicy.getPolicyName())
                .policyCode(savedPolicy.getPolicyCode())
                .policyType(savedPolicy.getPolicyType())
                .minAge(savedPolicy.getMinAge())
                .maxAge(savedPolicy.getMaxAge())
                .status(savedPolicy.getStatus())
                .build();
    }
    
    public List<PolicyResponse> getAllPolicies() {

        List<Policy> policies = policyRepository.findByStatus(PolicyStatus.ACTIVE);

        return policies.stream()
                .map(policy -> PolicyResponse.builder()
                        .policyId(policy.getPolicyId())
                        .companyId(policy.getInsurancePlan()
                                .getInsuranceCompany()
                                .getCompanyId())
                        .companyName(policy.getInsurancePlan()
                                .getInsuranceCompany()
                                .getCompanyName())
                        .planId(policy.getInsurancePlan().getPlanId())
                        .planName(policy.getInsurancePlan().getPlanName())
                        .policyName(policy.getPolicyName())
                        .policyCode(policy.getPolicyCode())
                        .policyType(policy.getPolicyType())
                        .minAge(policy.getMinAge())
                        .maxAge(policy.getMaxAge())
                        .status(policy.getStatus())
                        .build())
                .toList();
    }
    
    public PolicyResponse getPolicyById(Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with id: " + policyId));

        return PolicyResponse.builder()
                .policyId(policy.getPolicyId())
                .companyId(policy.getInsurancePlan()
                        .getInsuranceCompany()
                        .getCompanyId())
                .companyName(policy.getInsurancePlan()
                        .getInsuranceCompany()
                        .getCompanyName())
                .planId(policy.getInsurancePlan().getPlanId())
                .planName(policy.getInsurancePlan().getPlanName())
                .policyName(policy.getPolicyName())
                .policyCode(policy.getPolicyCode())
                .policyType(policy.getPolicyType())
                .minAge(policy.getMinAge())
                .maxAge(policy.getMaxAge())
                .status(policy.getStatus())
                .build();
    }
    
    public PolicyResponse updatePolicy(
            Long policyId,
            UpdatePolicyRequest request) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with id: " + policyId));

        if (policy.getStatus() == PolicyStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Inactive policy cannot be updated.");
        }

        InsurancePlan insurancePlan = insurancePlanRepository
                .findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insurance plan not found with id: " + request.getPlanId()));

        if (insurancePlan.getStatus() == PlanStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Cannot assign an inactive insurance plan.");
        }

        if (insurancePlan.getInsuranceCompany().getStatus() == CompanyStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Cannot assign a plan belonging to an inactive company.");
        }

        if (policyRepository.existsByPolicyCodeIgnoreCaseAndPolicyIdNot(
                request.getPolicyCode(), policyId)) {

            throw new ResourceAlreadyExistsException(
                    "Policy code already exists.");
        }

        if (policyRepository.existsByPolicyNameIgnoreCaseAndPolicyIdNot(
                request.getPolicyName(), policyId)) {

            throw new ResourceAlreadyExistsException(
                    "Policy name already exists.");
        }

        if (request.getMinAge() > request.getMaxAge()) {
            throw new BadRequestException(
                    "Minimum age cannot be greater than maximum age.");
        }

        policy.setInsurancePlan(insurancePlan);
        policy.setPolicyName(request.getPolicyName());
        policy.setPolicyCode(request.getPolicyCode());
        policy.setPolicyType(request.getPolicyType());
        policy.setMinAge(request.getMinAge());
        policy.setMaxAge(request.getMaxAge());

        Policy updatedPolicy = policyRepository.save(policy);

        return PolicyResponse.builder()
                .policyId(updatedPolicy.getPolicyId())
                .companyId(updatedPolicy.getInsurancePlan()
                        .getInsuranceCompany().getCompanyId())
                .companyName(updatedPolicy.getInsurancePlan()
                        .getInsuranceCompany().getCompanyName())
                .planId(updatedPolicy.getInsurancePlan().getPlanId())
                .planName(updatedPolicy.getInsurancePlan().getPlanName())
                .policyName(updatedPolicy.getPolicyName())
                .policyCode(updatedPolicy.getPolicyCode())
                .policyType(updatedPolicy.getPolicyType())
                .minAge(updatedPolicy.getMinAge())
                .maxAge(updatedPolicy.getMaxAge())
                .status(updatedPolicy.getStatus())
                .build();
    }
    
    public PolicyResponse updatePolicyStatus(
            Long policyId,
            UpdatePolicyStatusRequest request) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with id: " + policyId));

        policy.setStatus(request.getStatus());

        Policy updatedPolicy = policyRepository.save(policy);

        return PolicyResponse.builder()
                .policyId(updatedPolicy.getPolicyId())
                .companyId(updatedPolicy.getInsurancePlan()
                        .getInsuranceCompany()
                        .getCompanyId())
                .companyName(updatedPolicy.getInsurancePlan()
                        .getInsuranceCompany()
                        .getCompanyName())
                .planId(updatedPolicy.getInsurancePlan().getPlanId())
                .planName(updatedPolicy.getInsurancePlan().getPlanName())
                .policyName(updatedPolicy.getPolicyName())
                .policyCode(updatedPolicy.getPolicyCode())
                .policyType(updatedPolicy.getPolicyType())
                .minAge(updatedPolicy.getMinAge())
                .maxAge(updatedPolicy.getMaxAge())
                .status(updatedPolicy.getStatus())
                .build();
    }
    
    public void deletePolicy(Long policyId) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with id: " + policyId));

        if (policy.getStatus() == PolicyStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Policy is already inactive.");
        }

        policy.setStatus(PolicyStatus.INACTIVE);

        policyRepository.save(policy);
    }

    	
    public List<PolicyResponse> getPoliciesByPlan(Long planId) {
    	// Validate active plan
    	insurancePlanRepository.findByPlanIdAndStatus(
    	        planId,
    	        PlanStatus.ACTIVE)
    	    .orElseThrow(() ->
    	        new ResourceNotFoundException("Active plan not found"));

    	// Fetch active policies
    	List<Policy> policies =
    	        policyRepository.findByInsurancePlanPlanIdAndStatus(
    	                planId,
    	                PolicyStatus.ACTIVE);
    	
    	 return policies.stream()
                 .map(policy -> PolicyResponse.builder()
                         .policyId(policy.getPolicyId())
                         .companyId(policy.getInsurancePlan()
                                 .getInsuranceCompany()
                                 .getCompanyId())
                         .companyName(policy.getInsurancePlan()
                                 .getInsuranceCompany()
                                 .getCompanyName())
                         .planId(policy.getInsurancePlan().getPlanId())
                         .planName(policy.getInsurancePlan().getPlanName())
                         .policyName(policy.getPolicyName())
                         .policyCode(policy.getPolicyCode())
                         .policyType(policy.getPolicyType())
                         .minAge(policy.getMinAge())
                         .maxAge(policy.getMaxAge())
                         .status(policy.getStatus())
                         .build())
                 .toList();
    }
  
    public PolicyDetailsResponse getPolicyDetails(Long policyId) {

        Policy policy = policyRepository
                .findByPolicyIdAndStatus(policyId, PolicyStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Policy not found"));

        List<PolicyCoverage> coverages =
                policyCoverageRepository.findByPolicyPolicyIdAndStatus(
                        policyId,
                        CoverageStatus.ACTIVE);

        List<PolicyExclusion> exclusions =
                policyExclusionRepository.findByPolicyPolicyIdAndStatus(
                        policyId,
                        ExclusionStatus.ACTIVE);

        return mapToDetailsResponse(
                policy,
                coverages,
                exclusions);
    }
    
    private PolicyDetailsResponse mapToDetailsResponse(
            Policy policy,
            List<PolicyCoverage> coverages,
            List<PolicyExclusion> exclusions) {

        InsurancePlan plan = policy.getInsurancePlan();

        InsuranceCompany company = plan.getInsuranceCompany();

        return PolicyDetailsResponse.builder()

                // Company
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())

                // Plan
                .planId(plan.getPlanId())
                .planName(plan.getPlanName())
                .planDescription(plan.getDescription())
                .coverageAmount(plan.getCoverageAmount())
                .premiumAmount(plan.getPremiumAmount())
                .policyTermInMonths(plan.getPolicyTermInMonths())
                .waitingPeriodInDays(plan.getWaitingPeriodInDays())

                // Policy
                .policyId(policy.getPolicyId())
                .policyName(policy.getPolicyName())
                .policyCode(policy.getPolicyCode())
                .policyType(policy.getPolicyType())
                .minAge(policy.getMinAge())
                .maxAge(policy.getMaxAge())
                .status(policy.getStatus())

                // Coverages
                .coverages(
                        coverages.stream()
                                .map(this::mapCoverageResponse)
                                .toList())

                // Exclusions
                .exclusions(
                        exclusions.stream()
                                .map(this::mapExclusionResponse)
                                .toList())

                .build();
    }
    
    private PolicyCoverageResponse mapCoverageResponse(
            PolicyCoverage coverage) {

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

    private PolicyExclusionResponse mapExclusionResponse(
            PolicyExclusion exclusion) {

        return PolicyExclusionResponse.builder()
                .exclusionId(exclusion.getExclusionId())
                .policyId(exclusion.getPolicy().getPolicyId())
                .policyName(exclusion.getPolicy().getPolicyName())
                .exclusionName(exclusion.getExclusionName())
                .description(exclusion.getDescription())
                .status(exclusion.getStatus())
                .build();
    }

    


   
            
}
