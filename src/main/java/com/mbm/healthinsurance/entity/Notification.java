package com.mbm.healthinsurance.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.mbm.healthinsurance.enums.NotificationStatus;
import com.mbm.healthinsurance.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    @Column(name = "title", nullable = false, length = 150)
    private String title;


    @Column(name = "message", nullable = false, length = 500)
    private String message;


    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    private NotificationType notificationType;


    /*
     * Example:
     * PAYMENT
     * CLAIM
     * POLICY
     */
    @Column(name = "reference_type", length = 50)
    private String referenceType;


    /*
     * Example:
     * paymentId
     * claimId
     * customerPolicyId
     */
    @Column(name = "reference_id")
    private Long referenceId;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private NotificationStatus status;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "read_at")
    private LocalDateTime readAt;

}