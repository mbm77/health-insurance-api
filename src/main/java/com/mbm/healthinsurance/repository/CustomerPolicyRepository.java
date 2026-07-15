package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.model.CustomerPolicy;

public interface CustomerPolicyRepository extends JpaRepository<CustomerPolicy, Long> {

	List<CustomerPolicy> findByCustomerUserUsername(String username);

	Optional<CustomerPolicy> findByPolicyNumber(String policyNumber);

	boolean existsByPolicyNumber(String policyNumber);

	List<CustomerPolicy> findByStatus(CustomerPolicyStatus status);

	List<CustomerPolicy> findByCustomerCustomerId(Long customerId);

	boolean existsByCustomerCustomerIdAndPolicyPolicyIdAndStatus(Long customerId, Long policyId,
			CustomerPolicyStatus status);

}