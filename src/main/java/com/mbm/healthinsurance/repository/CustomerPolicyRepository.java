package com.mbm.healthinsurance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.Policy;
import com.mbm.healthinsurance.enums.CustomerPolicyStatus;

public interface CustomerPolicyRepository extends JpaRepository<CustomerPolicy, Long> {

	List<CustomerPolicy> findByCustomerUserUsername(String username);

	Optional<CustomerPolicy> findByPolicyNumber(String policyNumber);

	boolean existsByPolicyNumber(String policyNumber);

	List<CustomerPolicy> findByStatus(CustomerPolicyStatus status);

	List<CustomerPolicy> findByCustomerCustomerId(Long customerId);

	boolean existsByCustomerCustomerIdAndPolicyPolicyIdAndStatus(Long customerId, Long policyId,
			CustomerPolicyStatus status);

	boolean existsByCustomerAndPolicy(Customer customer, Policy policy);

	List<CustomerPolicy> findByCustomer(Customer customer);

	Optional<CustomerPolicy> findByCustomerPolicyIdAndCustomer(
			Long customerPolicyId,
			Customer customer);
	
	long countByCustomer(Customer customer);

	long countByCustomerAndStatus(
	        Customer customer,
	        CustomerPolicyStatus status);
	
	List<CustomerPolicy> findByCustomerAndExpiryDateBetween(
	        Customer customer,
	        LocalDate startDate,
	        LocalDate endDate);
	
	long countByStatus(CustomerPolicyStatus status);
	
	List<CustomerPolicy> findByCustomerOrderByPurchaseDateDesc(
	        Customer customer);
	
	Optional<CustomerPolicy> findByCustomerPolicyIdAndCustomerCustomerId(
	        Long customerPolicyId,
	        Long customerId);
	
	
	
	
	
	

}