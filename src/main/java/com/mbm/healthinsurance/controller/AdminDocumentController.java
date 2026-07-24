package com.mbm.healthinsurance.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.response.AdminDocumentResponse;
import com.mbm.healthinsurance.dto.response.DocumentDownloadResponse;
import com.mbm.healthinsurance.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDocumentController {

	private final DocumentService documentService;

	@GetMapping
	public List<AdminDocumentResponse> getAllDocuments() {

		return documentService.getAllDocuments();
	}

	@GetMapping("/customer-policies/{customerPolicyId}/documents")
	public List<AdminDocumentResponse> getDocumentsByCustomerPolicy(

			@PathVariable Long customerPolicyId) {

		return documentService.getDocumentsByCustomerPolicy(
				customerPolicyId);
	}

	@GetMapping("/customer-policies/{customerPolicyId}/documents/{documentId}")
	public AdminDocumentResponse getDocumentByPolicy(

			@PathVariable Long customerPolicyId,

			@PathVariable Long documentId) {

		return documentService.getDocumentByPolicy(
				customerPolicyId,
				documentId);
	}

	@GetMapping("/customer-policies/{customerPolicyId}/documents/{documentId}/download")
	public ResponseEntity<byte[]> downloadDocument(

			@PathVariable Long customerPolicyId,

			@PathVariable Long documentId)

			throws IOException {

		DocumentDownloadResponse response = documentService.downloadDocument(
				customerPolicyId,
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

	@DeleteMapping("/customer-policies/{customerPolicyId}/documents/{documentId}")
	public ResponseEntity<String> deleteDocument(

			@PathVariable Long customerPolicyId,

			@PathVariable Long documentId) {

		documentService.deleteDocument(
				customerPolicyId,
				documentId);

		return ResponseEntity.ok(
				"Document deleted successfully");
	}
}
