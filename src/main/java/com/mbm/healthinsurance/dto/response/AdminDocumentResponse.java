package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.DocumentStatus;
import com.mbm.healthinsurance.enums.DocumentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDocumentResponse {

    private Long documentId;

    private String documentNumber;

    private Long customerId;

    private String customerName;

    private Long customerPolicyId;

    private String policyNumber;

    private String policyName;

    private Long claimId;

    private String claimNumber;

    private DocumentType documentType;

    private DocumentStatus status;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    private LocalDateTime uploadedAt;
}