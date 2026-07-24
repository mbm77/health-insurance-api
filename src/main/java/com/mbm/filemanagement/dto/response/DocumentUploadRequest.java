package com.mbm.filemanagement.dto.response;

import com.mbm.healthinsurance.enums.DocumentType;

import lombok.Data;

@Data
public class DocumentUploadRequest {

    private DocumentType documentType;

    private Long customerPolicyId;

    private Long claimId;
}