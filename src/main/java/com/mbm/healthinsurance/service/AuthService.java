package com.mbm.healthinsurance.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.healthinsurance.dto.request.LoginRequest;
import com.mbm.healthinsurance.dto.request.RegisterRequest;
import com.mbm.healthinsurance.dto.response.LoginResponse;
import com.mbm.healthinsurance.dto.response.RegisterResponse;
import com.mbm.healthinsurance.exception.InvalidCredentialsException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.exception.UserAlreadyExistsException;
import com.mbm.healthinsurance.model.Customer;
import com.mbm.healthinsurance.model.Role;
import com.mbm.healthinsurance.model.User;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.RoleRepository;
import com.mbm.healthinsurance.repository.UserRepository;
import com.mbm.healthinsurance.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	@Transactional //If saving Customer fails, the User insert will also roll back.
	public RegisterResponse register(RegisterRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new UserAlreadyExistsException("Username already exists");
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException("Email already exists");
		}

		Role customerRole = roleRepository.findByRoleName("CUSTOMER")
		        .orElseThrow(() ->
		                new ResourceNotFoundException("CUSTOMER role not found"));

		User user = User.builder().username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword())).email(request.getEmail())
				.phone(request.getPhone()).enabled(true).roles(Set.of(customerRole)).build();

		userRepository.save(user);

		Customer customer = Customer.builder().user(user).build();

		customerRepository.save(customer);

		return RegisterResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.CREATED.value())
				.message("Customer registered successfully").build();
	}

	public LoginResponse login(LoginRequest request) {

	    authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    request.getUsername(),
	                    request.getPassword()));

	    User user = userRepository.findByUsername(request.getUsername())
	            .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
	    
	    /*
	      if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
					throw new InvalidCredentialsException("Invalid username or password");
		}
	     */

	    String accessToken = jwtService.generateToken(user);

	    return LoginResponse.builder()
	            .timestamp(LocalDateTime.now())
	            .status(HttpStatus.OK.value())
	            .message("Login successful")
	            .accessToken(accessToken)
	            .tokenType("Bearer")
	            .build();
	}
	
	
	
	

}