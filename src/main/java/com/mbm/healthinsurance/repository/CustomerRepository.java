package com.mbm.healthinsurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.model.Customer;
import com.mbm.healthinsurance.model.User;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
	Optional<Customer> findByUserUsername(String username);
	Optional<Customer> findByUser(User user);
	boolean existsByAadhaarNumberAndCustomerIdNot(String aadhaarNumber, Long customerId);

	boolean existsByPanNumberAndCustomerIdNot(String panNumber, Long customerId);
	//Optional<Customer> findByEmail(String email);

}