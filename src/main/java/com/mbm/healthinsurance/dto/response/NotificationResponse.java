package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.NotificationStatus;
import com.mbm.healthinsurance.enums.NotificationType;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class NotificationResponse {

    private Long notificationId;

    private String title;

    private String message;

    private NotificationType notificationType;

    private NotificationStatus status;

    private String referenceType;

    private Long referenceId;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

}