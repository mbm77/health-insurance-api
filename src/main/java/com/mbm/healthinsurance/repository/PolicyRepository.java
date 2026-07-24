package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Policy;
import com.mbm.healthinsurance.enums.PolicyStatus;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

	boolean existsByPolicyCodeIgnoreCase(String policyCode);

	boolean existsByPolicyNameIgnoreCase(String policyName);

	List<Policy> findByStatus(PolicyStatus status);

	List<Policy> findByStatusOrderByPolicyNameAsc(PolicyStatus status);

	List<Policy> findAllByOrderByPolicyIdDesc();

	boolean existsByPolicyCodeIgnoreCaseAndPolicyIdNot(String policyCode, Long policyId);

	boolean existsByPolicyNameIgnoreCaseAndPolicyIdNot(String policyName, Long policyId);

	List<Policy> findByInsurancePlanPlanIdAndStatus(Long planId, PolicyStatus status);

	Optional<Policy> findByPolicyIdAndStatus(Long planId, PolicyStatus status);
	
	long countByStatus(PolicyStatus status);
	
	

}