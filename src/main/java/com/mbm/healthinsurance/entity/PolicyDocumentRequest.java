package com.mbm.healthinsurance.entity;


import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.DocumentRequestStatus;

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
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "policy_document_requests")
@Getter
@Setter
public class PolicyDocumentRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "customer_policy_id",
        nullable = false
    )
    private CustomerPolicy customerPolicy;


    @Column(nullable = false, length = 500)
    private String remarks;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentRequestStatus status;


    private LocalDateTime requestedAt;


    private LocalDateTime updatedAt;
}
