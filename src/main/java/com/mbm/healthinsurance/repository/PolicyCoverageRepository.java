package com.mbm.healthinsurance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Policy;
import com.mbm.healthinsurance.entity.PolicyCoverage;
import com.mbm.healthinsurance.enums.CoverageStatus;
import com.mbm.healthinsurance.enums.PolicyStatus;

public interface PolicyCoverageRepository extends JpaRepository<PolicyCoverage, Long> {

	boolean existsByPolicyPolicyIdAndCoverageNameIgnoreCase(Long policyId, String coverageName);

	boolean existsByPolicyPolicyIdAndCoverageNameIgnoreCaseAndCoverageIdNot(Long policyId, String coverageName,
			Long coverageId);

	List<PolicyCoverage> findByPolicyPolicyId(Long policyId);

	List<PolicyCoverage> findByPolicyStatus(PolicyStatus status);

	List<PolicyCoverage> findByPolicyPolicyIdAndStatus(Long policyId, CoverageStatus status);

}