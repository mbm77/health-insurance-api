package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.ResolveAppealRequest;
import com.mbm.healthinsurance.dto.response.AdminAppealDetailsResponse;
import com.mbm.healthinsurance.dto.response.AdminAppealResponse;
import com.mbm.healthinsurance.dto.response.AppealResponse;
import com.mbm.healthinsurance.dto.response.RejectAppealRequest;
import com.mbm.healthinsurance.service.AppealService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/appeals")
@RequiredArgsConstructor
public class AdminAppealController {

	private final AppealService appealService;

	@GetMapping
	public ResponseEntity<List<AdminAppealResponse>> getAllAppeals() {
		return ResponseEntity.ok(appealService.getAllAppeals());
	}

	@GetMapping("/{appealId}")
	public ResponseEntity<AdminAppealDetailsResponse> getAppealById(
			@PathVariable Long appealId) {

		return ResponseEntity.ok(appealService.getAppealById(appealId));
	}

	@PutMapping("/{appealId}/review")
	public ResponseEntity<AppealResponse> reviewAppeal(
			@PathVariable Long appealId) {

		return ResponseEntity.ok(appealService.reviewAppeal(appealId));
	}

	@PutMapping("/{appealId}/resolve")
	public ResponseEntity<AppealResponse> resolveAppeal(
			@PathVariable Long appealId,
			@Valid @RequestBody ResolveAppealRequest request) {

		return ResponseEntity.ok(
				appealService.resolveAppeal(appealId, request));
	}

	@PutMapping("/{appealId}/reject")
	public ResponseEntity<AppealResponse> rejectAppeal(
			@PathVariable Long appealId,
			@Valid @RequestBody RejectAppealRequest request) {

		return ResponseEntity.ok(
				appealService.rejectAppeal(appealId, request));
	}
}
