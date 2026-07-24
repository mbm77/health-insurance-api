package com.mbm.healthinsurance.service;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.UpdateAdminCustomerStatusRequest;
import com.mbm.healthinsurance.dto.request.UpdateCustomerProfileRequest;
import com.mbm.healthinsurance.dto.response.AdminCustomerResponse;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.User;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminCustomerService {
	 private final CustomerRepository customerRepository;
	 private final UserRepository userRepository;
	
	 public List<AdminCustomerResponse> getAllCustomers() {

	        List<Customer> customers = customerRepository.findAll();

	        return customers.stream()
	                .map(customer -> AdminCustomerResponse.builder()
	                        .customerId(customer.getCustomerId())
	                        .userId(customer.getUser().getUserId())
	                        .username(customer.getUser().getUsername())
	                        .email(customer.getUser().getEmail())
	                        .phone(customer.getUser().getPhone())
	                        .enabled(customer.getUser().isEnabled())
	                        .firstName(customer.getFirstName())
	                        .lastName(customer.getLastName())
	                        .dateOfBirth(customer.getDateOfBirth())
	                        .gender(customer.getGender())
	                        .aadhaarNumber(customer.getAadhaarNumber())
	                        .panNumber(customer.getPanNumber())
	                        .bloodGroup(customer.getBloodGroup())
	                        .maritalStatus(customer.getMaritalStatus())
	                        .annualIncome(customer.getAnnualIncome())
	                        .address(customer.getAddress())
	                        .city(customer.getCity())
	                        .state(customer.getState())
	                        .country(customer.getCountry())
	                        .pincode(customer.getPincode())
	                        .build())
	                .toList();
	    }
	 
	 public AdminCustomerResponse getCustomerById(Long customerId) {

		    Customer customer = customerRepository.findById(customerId)
		            .orElseThrow(() ->
		                    new ResourceNotFoundException(
		                            "Customer not found with id: " + customerId));

		    return AdminCustomerResponse.builder()
		            .customerId(customer.getCustomerId())
		            .userId(customer.getUser().getUserId())
		            .username(customer.getUser().getUsername())
		            .email(customer.getUser().getEmail())
		            .phone(customer.getUser().getPhone())
		            .enabled(customer.getUser().isEnabled())
		            .firstName(customer.getFirstName())
		            .lastName(customer.getLastName())
		            .dateOfBirth(customer.getDateOfBirth())
		            .gender(customer.getGender())
		            .aadhaarNumber(customer.getAadhaarNumber())
		            .panNumber(customer.getPanNumber())
		            .bloodGroup(customer.getBloodGroup())
		            .maritalStatus(customer.getMaritalStatus())
		            .annualIncome(customer.getAnnualIncome())
		            .address(customer.getAddress())
		            .city(customer.getCity())
		            .state(customer.getState())
		            .country(customer.getCountry())
		            .pincode(customer.getPincode())
		            .build();
		}
	 
	 public AdminCustomerResponse updateCustomerStatus(
		        Long customerId,
		        UpdateAdminCustomerStatusRequest request) {

		    Customer customer = customerRepository.findById(customerId)
		            .orElseThrow(() ->
		                    new ResourceNotFoundException(
		                            "Customer not found with id: " + customerId));

		    User user = customer.getUser();

		    user.setEnabled(request.getEnabled());

		    userRepository.save(user);

		    return AdminCustomerResponse.builder()
		            .customerId(customer.getCustomerId())
		            .userId(user.getUserId())
		            .username(user.getUsername())
		            .email(user.getEmail())
		            .phone(user.getPhone())
		            .enabled(user.isEnabled())
		            .firstName(customer.getFirstName())
		            .lastName(customer.getLastName())
		            .dateOfBirth(customer.getDateOfBirth())
		            .gender(customer.getGender())
		            .aadhaarNumber(customer.getAadhaarNumber())
		            .panNumber(customer.getPanNumber())
		            .bloodGroup(customer.getBloodGroup())
		            .maritalStatus(customer.getMaritalStatus())
		            .annualIncome(customer.getAnnualIncome())
		            .address(customer.getAddress())
		            .city(customer.getCity())
		            .state(customer.getState())
		            .country(customer.getCountry())
		            .pincode(customer.getPincode())
		            .build();
		}
	 
	 public AdminCustomerResponse updateAdminCustomer(
		        Long customerId,
		        UpdateCustomerProfileRequest request) {

		    Customer customer = customerRepository.findById(customerId)
		            .orElseThrow(() ->
		                    new ResourceNotFoundException(
		                            "Customer not found with id: " + customerId));

		    customer.setFirstName(request.getFirstName());
		    customer.setLastName(request.getLastName());
		    customer.setDateOfBirth(request.getDateOfBirth());
		    customer.setGender(request.getGender());
		    customer.setAadhaarNumber(request.getAadhaarNumber());
		    customer.setPanNumber(request.getPanNumber());
		    customer.setBloodGroup(request.getBloodGroup());
		    customer.setMaritalStatus(request.getMaritalStatus());
		    customer.setAnnualIncome(request.getAnnualIncome());
		    customer.setAddress(request.getAddress());
		    customer.setCity(request.getCity());
		    customer.setState(request.getState());
		    customer.setCountry(request.getCountry());
		    customer.setPincode(request.getPincode());

		    Customer updatedCustomer = customerRepository.save(customer);

		    return AdminCustomerResponse.builder()
		            .customerId(updatedCustomer.getCustomerId())
		            .userId(updatedCustomer.getUser().getUserId())
		            .username(updatedCustomer.getUser().getUsername())
		            .email(updatedCustomer.getUser().getEmail())
		            .phone(updatedCustomer.getUser().getPhone())
		            .enabled(updatedCustomer.getUser().isEnabled())
		            .firstName(updatedCustomer.getFirstName())
		            .lastName(updatedCustomer.getLastName())
		            .dateOfBirth(updatedCustomer.getDateOfBirth())
		            .gender(updatedCustomer.getGender())
		            .aadhaarNumber(updatedCustomer.getAadhaarNumber())
		            .panNumber(updatedCustomer.getPanNumber())
		            .bloodGroup(updatedCustomer.getBloodGroup())
		            .maritalStatus(updatedCustomer.getMaritalStatus())
		            .annualIncome(updatedCustomer.getAnnualIncome())
		            .address(updatedCustomer.getAddress())
		            .city(updatedCustomer.getCity())
		            .state(updatedCustomer.getState())
		            .country(updatedCustomer.getCountry())
		            .pincode(updatedCustomer.getPincode())
		            .build();
		}

	 
	 

}
