package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.PolicyRenewal;

@Repository
public interface PolicyRenewalRepository
		extends JpaRepository<PolicyRenewal, Long> {

	Optional<PolicyRenewal> findTopByCustomerPolicyOrderByRenewalNumberDesc(
			CustomerPolicy customerPolicy);

	List<PolicyRenewal> findByCustomerPolicyOrderByRenewalNumberDesc(
			CustomerPolicy customerPolicy);

}