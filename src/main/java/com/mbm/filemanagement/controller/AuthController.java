package com.mbm.filemanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.filemanagement.dto.request.LoginRequest;
import com.mbm.filemanagement.dto.request.RegisterRequest;
import com.mbm.filemanagement.dto.response.LoginResponse;
import com.mbm.filemanagement.dto.response.RegisterResponse;
import com.mbm.filemanagement.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

		authService.login(request);

		return ResponseEntity.ok(authService.login(request));
	}
}