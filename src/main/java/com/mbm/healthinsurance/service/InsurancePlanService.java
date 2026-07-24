package com.mbm.healthinsurance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.CreateInsurancePlanRequest;
import com.mbm.healthinsurance.dto.request.UpdateInsurancePlanRequest;
import com.mbm.healthinsurance.dto.request.UpdateInsurancePlanStatusRequest;
import com.mbm.healthinsurance.dto.response.InsurancePlanResponse;
import com.mbm.healthinsurance.entity.InsuranceCompany;
import com.mbm.healthinsurance.entity.InsurancePlan;
import com.mbm.healthinsurance.enums.CompanyStatus;
import com.mbm.healthinsurance.enums.PlanStatus;
import com.mbm.healthinsurance.exception.ResourceAlreadyExistsException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.InsuranceCompanyRepository;
import com.mbm.healthinsurance.repository.InsurancePlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsurancePlanService {

    private final InsurancePlanRepository planRepository;
    private final InsuranceCompanyRepository companyRepository;
    
    public InsurancePlanResponse createPlan(CreateInsurancePlanRequest request) {

        InsuranceCompany company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Insurance company not found with id: " + request.getCompanyId()));

        if (company.getStatus() == CompanyStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Cannot create plan for an inactive company.");
        }

        boolean exists = planRepository
                .existsByInsuranceCompanyCompanyIdAndPlanNameIgnoreCase(
                        request.getCompanyId(),
                        request.getPlanName());

        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "Plan name already exists for this company.");
        }

        InsurancePlan plan = InsurancePlan.builder()
                .insuranceCompany(company)
                .planName(request.getPlanName())
                .description(request.getDescription())
                .coverageAmount(request.getCoverageAmount())
                .premiumAmount(request.getPremiumAmount())
                .policyTermInMonths(request.getPolicyTermInMonths())
                .waitingPeriodInDays(request.getWaitingPeriodInDays())
                .status(PlanStatus.ACTIVE)
                .build();

        plan = planRepository.save(plan);

        return buildPlanResponse(plan);
    }
    
    public List<InsurancePlanResponse> getAllPlans() {

        return planRepository.findByStatus(PlanStatus.ACTIVE)
                .stream()
                .map(this::buildPlanResponse)
                .toList();
    }
    
    public InsurancePlanResponse getPlanById(Long planId) {

        InsurancePlan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Insurance plan not found with id: " + planId));

        return buildPlanResponse(plan);
    }
    
    public InsurancePlanResponse getPlanByIdAndStatus(Long planId) {

        InsurancePlan plan = planRepository.findByPlanIdAndStatus(planId, PlanStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Insurance plan not found with id: " + planId));

        return buildPlanResponse(plan);
    }
    
    public InsurancePlanResponse updatePlan(
            Long planId,
            UpdateInsurancePlanRequest request) {

        InsurancePlan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Insurance plan not found with id: " + planId));

        if (plan.getStatus() == PlanStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Inactive plan cannot be updated. Activate it first.");
        }

        if (plan.getInsuranceCompany().getStatus() == CompanyStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Cannot update a plan belonging to an inactive company.");
        }

        boolean exists = planRepository
                .existsByInsuranceCompanyCompanyIdAndPlanNameIgnoreCaseAndPlanIdNot(
                        plan.getInsuranceCompany().getCompanyId(),
                        request.getPlanName(),
                        planId);

        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "Plan name already exists for this company.");
        }

        plan.setPlanName(request.getPlanName());
        plan.setDescription(request.getDescription());
        plan.setCoverageAmount(request.getCoverageAmount());
        plan.setPremiumAmount(request.getPremiumAmount());
        plan.setPolicyTermInMonths(request.getPolicyTermInMonths());
        plan.setWaitingPeriodInDays(request.getWaitingPeriodInDays());

        plan = planRepository.save(plan);

        return buildPlanResponse(plan);
    }
    
    private InsurancePlanResponse buildPlanResponse(InsurancePlan plan) {

        return InsurancePlanResponse.builder()
                .planId(plan.getPlanId())
                .companyId(plan.getInsuranceCompany().getCompanyId())
                .companyName(plan.getInsuranceCompany().getCompanyName())
                .planName(plan.getPlanName())
                .description(plan.getDescription())
                .coverageAmount(plan.getCoverageAmount())
                .premiumAmount(plan.getPremiumAmount())
                .policyTermInMonths(plan.getPolicyTermInMonths())
                .waitingPeriodInDays(plan.getWaitingPeriodInDays())
                .status(plan.getStatus())
                .build();
    }
    
    public InsurancePlanResponse updatePlanStatus(
            Long planId,
            UpdateInsurancePlanStatusRequest request) {

        InsurancePlan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Insurance plan not found with id: " + planId));

        if (plan.getStatus() == request.getStatus()) {
            throw new ResourceAlreadyExistsException(
                    "Insurance plan is already " + request.getStatus());
        }

        // Cannot activate a plan if its company is inactive
        if (request.getStatus() == PlanStatus.ACTIVE
                && plan.getInsuranceCompany().getStatus() == CompanyStatus.INACTIVE) {

            throw new ResourceAlreadyExistsException(
                    "Cannot activate a plan because its insurance company is inactive.");
        }

        plan.setStatus(request.getStatus());

        plan = planRepository.save(plan);

        return buildPlanResponse(plan);
    }
    
    public void deletePlan(Long planId) {

        InsurancePlan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Insurance plan not found with id: " + planId));

        if (plan.getStatus() == PlanStatus.INACTIVE) {
            throw new ResourceAlreadyExistsException(
                    "Insurance plan is already inactive.");
        }

        plan.setStatus(PlanStatus.INACTIVE);

        planRepository.save(plan);
    }
    
    public List<InsurancePlanResponse> getPlansByCompany(Long companyId) {

        InsuranceCompany company = companyRepository
                .findByCompanyIdAndStatus(
                        companyId,
                        CompanyStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active company not found with id: " + companyId));

        List<InsurancePlan> plans =
                planRepository.findByInsuranceCompanyAndStatus(
                        company,
                        PlanStatus.ACTIVE);

        return plans.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    private InsurancePlanResponse mapToResponse(InsurancePlan plan) {

        return InsurancePlanResponse.builder()
                .planId(plan.getPlanId())
                .companyId(plan.getInsuranceCompany().getCompanyId())
                .companyName(plan.getInsuranceCompany().getCompanyName())
                .planName(plan.getPlanName())
                .description(plan.getDescription())
                .coverageAmount(plan.getCoverageAmount())
                .premiumAmount(plan.getPremiumAmount())
                .policyTermInMonths(plan.getPolicyTermInMonths())
                .waitingPeriodInDays(plan.getWaitingPeriodInDays())
                .status(plan.getStatus())
                .build();
    }

}