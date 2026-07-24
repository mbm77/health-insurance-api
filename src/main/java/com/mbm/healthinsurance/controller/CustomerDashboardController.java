package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.response.CustomerDashboardResponse;
import com.mbm.healthinsurance.dto.response.ExpiringPolicyResponse;
import com.mbm.healthinsurance.dto.response.RecentClaimResponse;
import com.mbm.healthinsurance.dto.response.RecentNotificationResponse;
import com.mbm.healthinsurance.dto.response.RecentPaymentResponse;
import com.mbm.healthinsurance.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomerDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/customers/dashboard")
    public CustomerDashboardResponse getCustomerDashboard(
            Authentication authentication) {

        return dashboardService.getCustomerDashboard(
                authentication);
    }
    
    @GetMapping("/customers/dashboard/recent-claims")
    public List<RecentClaimResponse> getRecentClaims(
            Authentication authentication) {

        return dashboardService.getRecentClaims(
                authentication);
    }
    
    @GetMapping("/customers/dashboard/recent-payments")
    public List<RecentPaymentResponse> getRecentPayments(
            Authentication authentication) {


        return dashboardService.getRecentPayments(
                authentication);
    }
    
    @GetMapping("/customers/dashboard/recent-notifications")
    public List<RecentNotificationResponse> getRecentNotifications(
            Authentication authentication) {


        return dashboardService.getRecentNotifications(
                authentication);
    }
    
    @GetMapping("/customers/dashboard/expiring-policies")
    public List<ExpiringPolicyResponse> getExpiringPolicies(
            Authentication authentication) {


        return dashboardService.getExpiringPolicies(
                authentication);
    }
}