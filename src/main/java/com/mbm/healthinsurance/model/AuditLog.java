package com.mbm.healthinsurance.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditLogId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String action;

    private String entityName;

    private Long entityId;

    private String ipAddress;

    private LocalDateTime createdAt;
}