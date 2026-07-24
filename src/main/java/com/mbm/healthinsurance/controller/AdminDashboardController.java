package com.mbm.healthinsurance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.response.AdminDashboardResponse;
import com.mbm.healthinsurance.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin/dashboard")
    public AdminDashboardResponse getAdminDashboard() {

        return dashboardService.getAdminDashboard();
    }
    
    
}