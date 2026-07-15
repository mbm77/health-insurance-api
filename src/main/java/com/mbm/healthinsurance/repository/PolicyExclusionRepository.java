package com.mbm.healthinsurance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.enums.ExclusionStatus;
import com.mbm.healthinsurance.model.PolicyExclusion;

public interface PolicyExclusionRepository extends JpaRepository<PolicyExclusion, Long> {

	boolean existsByPolicyPolicyIdAndExclusionNameIgnoreCase(Long policyId, String exclusionName);

	boolean existsByPolicyPolicyIdAndExclusionNameIgnoreCaseAndExclusionIdNot(Long policyId, String exclusionName,
			Long exclusionId);

	List<PolicyExclusion> findByStatus(ExclusionStatus status);

	List<PolicyExclusion> findByPolicyPolicyIdAndStatus(Long policyId, ExclusionStatus status);

}