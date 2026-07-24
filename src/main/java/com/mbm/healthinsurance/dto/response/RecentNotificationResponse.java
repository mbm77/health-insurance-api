package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.NotificationStatus;
import com.mbm.healthinsurance.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentNotificationResponse {

    private Long notificationId;

    private String title;

    private String message;

    private NotificationType notificationType;

    private NotificationStatus status;

    private LocalDateTime createdAt;

}