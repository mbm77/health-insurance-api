package com.mbm.filemanagement.dto.response;

import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.DocumentStatus;
import com.mbm.healthinsurance.enums.DocumentType;

import lombok.Data;

@Data
public class DocumentResponse {

    private Long documentId;

    private String documentNumber;

    private DocumentType documentType;

    private DocumentStatus status;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    private LocalDateTime uploadedAt;
}