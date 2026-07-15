package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.CreateAdminUserRequest;
import com.mbm.healthinsurance.dto.request.UpdateAdminUserPasswordRequest;
import com.mbm.healthinsurance.dto.request.UpdateAdminUserRequest;
import com.mbm.healthinsurance.dto.request.UpdateAdminUserStatusRequest;
import com.mbm.healthinsurance.dto.response.AdminUserResponse;
import com.mbm.healthinsurance.service.AdminUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

	private final AdminUserService adminUserService;
	

	@PostMapping
	public ResponseEntity<AdminUserResponse> createUser(@Valid @RequestBody CreateAdminUserRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(adminUserService.createUser(request));
	}

	@GetMapping
	public ResponseEntity<List<AdminUserResponse>> getAllUsers() {

		return ResponseEntity.ok(adminUserService.getAllUsers());
	}

	@GetMapping("/{userId}")
	public ResponseEntity<AdminUserResponse> getUserById(@PathVariable Long userId) {

		return ResponseEntity.ok(adminUserService.getUserById(userId));
	}

	@PutMapping("/{userId}")
	public ResponseEntity<AdminUserResponse> updateUser(@PathVariable Long userId,
			@Valid @RequestBody UpdateAdminUserRequest request) {
		return ResponseEntity.ok(adminUserService.updateUser(userId, request));
	}
	
	@PatchMapping("/{userId}/status")
	public ResponseEntity<AdminUserResponse> updateUserStatus(@PathVariable Long userId,
			@Valid @RequestBody UpdateAdminUserStatusRequest request) {
		return ResponseEntity.ok(adminUserService.updateUserStatus(userId, request));
	}
	
	
	
	@PatchMapping("/{userId}/password")
	public ResponseEntity<AdminUserResponse> updateUserPassword(
	        @PathVariable Long userId,
	        @Valid @RequestBody UpdateAdminUserPasswordRequest request) {

	    return ResponseEntity.ok(
	            adminUserService.updateUserPassword(userId, request));
	}
	
	
	/*
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(
	        @PathVariable Long userId) {

	    adminUserService.deleteUser(userId);

	    return ResponseEntity.noContent().build();
	}
	*/
	
}