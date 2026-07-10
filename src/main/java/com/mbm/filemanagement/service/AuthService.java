package com.mbm.filemanagement.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbm.filemanagement.dto.request.LoginRequest;
import com.mbm.filemanagement.dto.request.RegisterRequest;
import com.mbm.filemanagement.dto.response.LoginResponse;
import com.mbm.filemanagement.dto.response.RegisterResponse;
import com.mbm.filemanagement.exception.InvalidCredentialsException;
import com.mbm.filemanagement.exception.UserAlreadyExistsException;
import com.mbm.filemanagement.exception.UserNotFoundException;
import com.mbm.filemanagement.model.User;
import com.mbm.filemanagement.repository.UserRepository;
import com.mbm.filemanagement.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtService jwtService;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public RegisterResponse register(RegisterRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new UserAlreadyExistsException("Username already exists");
		}

		User user = User.builder().username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword())).role("USER").build();

		userRepository.save(user);

		return RegisterResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.CREATED.value())
				.message("User registered successfully").build();

	}

	public LoginResponse login(LoginRequest request) {

		User user = userRepository.findByUsername(request.getUsername());

		if (user == null) {
			throw new UserNotFoundException("User not found");
		}

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid username or password");
		}

		String token = jwtService.generateToken(user.getUsername(), user.getRole());

		return LoginResponse.builder().accessToken(token).tokenType("Bearer").expiresIn(3600).build();
	}
}