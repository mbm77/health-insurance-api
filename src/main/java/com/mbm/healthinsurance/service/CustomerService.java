package com.mbm.healthinsurance.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.ChangePasswordRequest;
import com.mbm.healthinsurance.dto.request.UpdateCustomerProfileRequest;
import com.mbm.healthinsurance.dto.response.AdminCustomerResponse;
import com.mbm.healthinsurance.dto.response.CustomerProfileResponse;
import com.mbm.healthinsurance.dto.response.MessageResponse;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.model.Customer;
import com.mbm.healthinsurance.model.User;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository; 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerProfileResponse getProfile(String username) {

        Customer customer = customerRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        return CustomerProfileResponse.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getUser().getEmail())
                .phone(customer.getUser().getPhone())
                .build();
    }
    
    public CustomerProfileResponse updateProfile(
            String username,
            UpdateCustomerProfileRequest request) {

        Customer customer = customerRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setAadhaarNumber(request.getAadhaarNumber());
        customer.setPanNumber(request.getPanNumber());
        customer.setBloodGroup(request.getBloodGroup());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setOccupation(request.getOccupation());
        customer.setAnnualIncome(request.getAnnualIncome());

        customerRepository.save(customer);

        return CustomerProfileResponse.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getUser().getEmail())
                .phone(customer.getUser().getPhone())
                .build();
    }
    
    public AdminCustomerResponse getProfile() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

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
    
    
   
    public AdminCustomerResponse updateProfile(UpdateCustomerProfileRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setAadhaarNumber(request.getAadhaarNumber());
        customer.setPanNumber(request.getPanNumber());
        customer.setBloodGroup(request.getBloodGroup());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setOccupation(request.getOccupation());
        customer.setAnnualIncome(request.getAnnualIncome());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setCountry(request.getCountry());
        customer.setPincode(request.getPincode());

        customerRepository.save(customer);

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
                .occupation(customer.getOccupation())
                .annualIncome(customer.getAnnualIncome())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .country(customer.getCountry())
                .pincode(customer.getPincode())
                .build();
    }
    
    
    public MessageResponse changePassword(ChangePasswordRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // Verify new password and confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }

        // Prevent using the same password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return MessageResponse.builder()
                .message("Password changed successfully.")
                .build();
    }
}