package com.mbm.healthinsurance.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mbm.filemanagement.dto.response.DocumentResponse;
import com.mbm.healthinsurance.dto.response.DocumentDownloadResponse;
import com.mbm.healthinsurance.enums.DocumentType;
import com.mbm.healthinsurance.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers/documents")
@RequiredArgsConstructor
public class CustomerDocumentController {


    private final DocumentService documentService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentResponse uploadDocument(

            Authentication authentication,

            @RequestParam("file")
            MultipartFile file,

            @RequestParam("documentType")
            DocumentType documentType,

            @RequestParam("customerPolicyId")
            Long customerPolicyId,

            @RequestParam(value = "claimId", required = false)
            Long claimId)

            throws IOException {


        return documentService.uploadDocument(
                authentication,
                file,
                documentType,
                customerPolicyId,
                claimId);
    }
    
    @GetMapping
    public List<DocumentResponse> getMyDocuments(
            Authentication authentication) {


        return documentService
                .getMyDocuments(authentication);
    }
    
    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(

            Authentication authentication,

            @PathVariable Long documentId)

            throws IOException {


        DocumentDownloadResponse response =
                documentService
                .downloadDocument(
                        authentication,
                        documentId);



        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                response.getContentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" 
                        + response.getFileName()
                        + "\"")
                .contentLength(
                        response.getData().length)
                .body(response.getData());
    }
    
    @PostMapping(
            value = "/multiple",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<DocumentResponse> uploadMultipleDocuments(

            Authentication authentication,

            @RequestParam("files")
            MultipartFile[] files,

            @RequestParam("documentType")
            DocumentType documentType,

            @RequestParam("customerPolicyId")
            Long customerPolicyId,

            @RequestParam(value = "claimId", required = false)
            Long claimId)

            throws IOException {


        return documentService.uploadMultipleDocuments(
                authentication,
                files,
                documentType,
                customerPolicyId,
                claimId);
    }
}