package com.mbm.healthinsurance.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.response.NotificationResponse;
import com.mbm.healthinsurance.service.NotificationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/customers/notifications")
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(

            @AuthenticationPrincipal UserDetails userDetails) {


        return ResponseEntity.ok(
                notificationService.getMyNotifications(
                        userDetails.getUsername()
                )
        );
    }

}