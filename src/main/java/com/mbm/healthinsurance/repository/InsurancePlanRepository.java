package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.InsuranceCompany;
import com.mbm.healthinsurance.entity.InsurancePlan;
import com.mbm.healthinsurance.enums.PlanStatus;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Long> {

	Optional<InsurancePlan> findByPlanNameIgnoreCase(String planName);

	boolean existsByInsuranceCompanyCompanyIdAndPlanNameIgnoreCase(Long companyId, String planName);

	List<InsurancePlan> findByStatus(PlanStatus status);

	List<InsurancePlan> findByInsuranceCompanyCompanyId(Long companyId);

	boolean existsByInsuranceCompanyCompanyIdAndPlanNameIgnoreCaseAndPlanIdNot(Long companyId, String planName,
			Long planId);

	List<InsurancePlan> findByInsuranceCompanyAndStatus(InsuranceCompany insuranceCompany, PlanStatus status);

	Optional<InsurancePlan> findByPlanIdAndStatus(Long planId, PlanStatus status);
}