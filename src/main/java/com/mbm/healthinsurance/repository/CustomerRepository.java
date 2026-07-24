package com.mbm.healthinsurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.User;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
	Optional<Customer> findByUserUsername(String username);
	Optional<Customer> findByUser(User user);
	Optional<Customer> findByUserUserId(Long userId);
	boolean existsByAadhaarNumberAndCustomerIdNot(String aadhaarNumber, Long customerId);

	boolean existsByPanNumberAndCustomerIdNot(String panNumber, Long customerId);
	
	long countByUserEnabled(boolean enabled);
}