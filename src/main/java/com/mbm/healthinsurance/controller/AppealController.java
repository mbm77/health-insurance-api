package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.CreateAppealRequest;
import com.mbm.healthinsurance.dto.response.AppealDetailsResponse;
import com.mbm.healthinsurance.dto.response.AppealResponse;
import com.mbm.healthinsurance.dto.response.AppealSummaryResponse;
import com.mbm.healthinsurance.service.AppealService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers/appeals")
@RequiredArgsConstructor
@Validated
public class AppealController {

    private final AppealService appealService;

    @PostMapping
    public ResponseEntity<AppealResponse> createAppeal(
            Authentication authentication,
            @Valid @RequestBody CreateAppealRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appealService.createAppeal(authentication, request));
    }
    
    @GetMapping
    public ResponseEntity<List<AppealSummaryResponse>> getMyAppeals(
            Authentication authentication) {

        return ResponseEntity.ok(
                appealService.getMyAppeals(authentication));
    }
    
    @GetMapping("/{appealId}")
    public ResponseEntity<AppealDetailsResponse> getMyAppealById(
            Authentication authentication,
            @PathVariable Long appealId) {

        return ResponseEntity.ok(
                appealService.getMyAppealById(authentication, appealId));
    }
    
   
}