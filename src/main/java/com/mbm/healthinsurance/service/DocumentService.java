package com.mbm.healthinsurance.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mbm.filemanagement.dto.response.DocumentResponse;
import com.mbm.healthinsurance.dto.response.AdminDocumentResponse;
import com.mbm.healthinsurance.dto.response.DocumentDownloadResponse;
import com.mbm.healthinsurance.entity.Claim;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.Document;
import com.mbm.healthinsurance.enums.DocumentStatus;
import com.mbm.healthinsurance.enums.DocumentType;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.ClaimRepository;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.DocumentRepository;
import com.mbm.healthinsurance.storage.FileStorageService;
import com.mbm.healthinsurance.util.DocumentNumberGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final CustomerRepository customerRepository;

	private final CustomerPolicyRepository customerPolicyRepository;

	private final ClaimRepository claimRepository;

	private final DocumentRepository documentRepository;

	private final FileStorageService fileStorageService;

	private final DocumentNumberGenerator documentNumberGenerator;

	@Transactional
	public DocumentResponse uploadDocument(
			Authentication authentication,
			MultipartFile file,
			DocumentType documentType,
			Long customerPolicyId,
			Long claimId) throws IOException {

		String username = authentication.getName();

		/*
		 * 1. Find logged-in customer
		 */
		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found"));

		/*
		 * 2. Validate document type
		 */
		validateDocumentType(documentType);

		/*
		 * 3. Validate file
		 */

		validateFile(file);

		CustomerPolicy customerPolicy;
		Claim claim = null;

		/*
		 * 4. Validate customer policy
		 *    Policy is mandatory for every document
		 */

		customerPolicy = customerPolicyRepository
				.findByCustomerPolicyIdAndCustomer(
						customerPolicyId,
						customer)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Policy not found for customer"));

		/*
		 * 5. Validate claim if provided
		 */
		if (claimId != null) {

			claim = claimRepository
					.findByClaimIdAndCustomerPolicyCustomer(
							claimId,
							customer)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Claim not found for customer"));

			/*
			 * Derive policy from claim
			 */
			customerPolicy = claim.getCustomerPolicy();
		}

		/*
		 * 6. Generate document number
		 */
		String documentNumber = documentNumberGenerator
				.generateDocumentNumber();

		String extension = getExtension(
				file.getOriginalFilename());

		String storedFileName = documentNumber + extension;

		String filePath = null;

		try {

			/*
			 * 7. Store physical file
			 */
			filePath = fileStorageService
					.storeFile(
							file,
							"customers",
							storedFileName);

			/*
			 * 8. Save document metadata
			 */
			Document document = new Document();

			document.setDocumentNumber(
					documentNumber);

			document.setCustomer(
					customer);

			document.setCustomerPolicy(
					customerPolicy);

			document.setClaim(
					claim);

			document.setDocumentType(
					documentType);

			document.setStatus(
					DocumentStatus.UPLOADED);

			document.setOriginalFileName(
					file.getOriginalFilename());

			document.setStoredFileName(
					storedFileName);

			document.setContentType(
					file.getContentType());

			document.setFileExtension(
					extension);

			document.setFileSize(
					file.getSize());

			document.setFilePath(
					filePath);

			Document savedDocument = documentRepository.save(document);

			return mapToResponse(savedDocument);

		} catch (Exception e) {

			/*
			 * Cleanup file if DB save fails
			 */
			if (filePath != null) {

				fileStorageService
						.deleteFile(filePath);
			}

			throw e;
		}
	}

	private void validateFile(
			MultipartFile file) {

		if (file == null || file.isEmpty()) {

			throw new IllegalArgumentException(
					"File is required");
		}

		long maxSize = 10 * 1024 * 1024;

		if (file.getSize() > maxSize) {
			throw new BadRequestException(
					"File size should not exceed 10 MB");

		}

		List<String> allowedTypes = List.of(
				"application/pdf",
				"image/jpeg",
				"image/png");

		if (!allowedTypes.contains(
				file.getContentType())) {

			throw new IllegalArgumentException(
					"Only PDF, JPG and PNG files are allowed");
		}
	}

	private void validateDocumentType(
			DocumentType documentType) {

		if (documentType == null) {

			throw new IllegalArgumentException(
					"Document type is required");
		}
	}

	private String getExtension(
			String fileName) {

		if (fileName == null ||
				!fileName.contains(".")) {

			return "";
		}

		return fileName.substring(
				fileName.lastIndexOf("."));
	}

	private DocumentResponse mapToResponse(
			Document document) {

		DocumentResponse response = new DocumentResponse();

		response.setDocumentId(
				document.getDocumentId());

		response.setDocumentNumber(
				document.getDocumentNumber());

		response.setDocumentType(
				document.getDocumentType());

		response.setStatus(
				document.getStatus());

		response.setOriginalFileName(
				document.getOriginalFileName());

		response.setContentType(
				document.getContentType());

		response.setFileSize(
				document.getFileSize());

		response.setUploadedAt(
				document.getUploadedAt());

		return response;
	}

	public List<DocumentResponse> getMyDocuments(
			Authentication authentication) {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found"));

		List<Document> documents = documentRepository
				.findByCustomerOrderByUploadedAtDesc(
						customer);

		return documents.stream()
				.map(this::mapToResponse)
				.toList();
	}

	public DocumentDownloadResponse downloadDocument(
			Authentication authentication,
			Long documentId)
			throws IOException {

		String username = authentication.getName();

		Customer customer = customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found"));

		Document document = documentRepository
				.findByDocumentIdAndCustomer(
						documentId,
						customer)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Document not found"));

		byte[] file = fileStorageService
				.downloadFile(
						document.getFilePath());

		return new DocumentDownloadResponse(
				file,
				document.getOriginalFileName(),
				document.getContentType());
	}

	@Transactional
	public List<DocumentResponse> uploadMultipleDocuments(

			Authentication authentication,

			MultipartFile[] files,

			DocumentType documentType,

			Long customerPolicyId,

			Long claimId)

			throws IOException {

		if (files == null || files.length == 0) {

			throw new IllegalArgumentException(
					"At least one file is required");
		}

		List<DocumentResponse> responses = new ArrayList<>();

		// validateTotalUploadSize(files);

		for (MultipartFile file : files) {

			DocumentResponse response = uploadDocument(
					authentication,
					file,
					documentType,
					customerPolicyId,
					claimId);

			responses.add(response);
		}

		return responses;
	}

	public List<AdminDocumentResponse> getAllDocuments() {

		List<Document> documents = documentRepository
				.findAllByOrderByUploadedAtDesc();

		return documents.stream()
				.map(this::mapToAdminDocumentResponse)
				.toList();
	}

	private AdminDocumentResponse mapToAdminDocumentResponse(
			Document document) {

		AdminDocumentResponse response = new AdminDocumentResponse();

		response.setDocumentId(
				document.getDocumentId());

		response.setDocumentNumber(
				document.getDocumentNumber());

		response.setCustomerId(
				document.getCustomer().getCustomerId());

		response.setCustomerName(
				document.getCustomer().getFirstName()
						+ " "
						+ document.getCustomer().getLastName());

		response.setCustomerPolicyId(
				document.getCustomerPolicy().getCustomerPolicyId());

		response.setPolicyNumber(
				document.getCustomerPolicy().getPolicyNumber());

		response.setPolicyName(
				document.getCustomerPolicy()
						.getPolicy()
						.getPolicyName());

		if (document.getClaim() != null) {

			response.setClaimId(
					document.getClaim().getClaimId());

			response.setClaimNumber(
					document.getClaim().getClaimNumber());
		}

		response.setDocumentType(
				document.getDocumentType());

		response.setStatus(
				document.getStatus());

		response.setOriginalFileName(
				document.getOriginalFileName());

		response.setContentType(
				document.getContentType());

		response.setFileSize(
				document.getFileSize());

		response.setUploadedAt(
				document.getUploadedAt());

		return response;
	}
	
	public List<AdminDocumentResponse> getDocumentsByCustomerPolicy(
	        Long customerPolicyId) {

	    CustomerPolicy customerPolicy =
	            customerPolicyRepository
	                    .findById(customerPolicyId)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Customer policy not found"));

	    List<Document> documents =
	            documentRepository
	                    .findByCustomerPolicyOrderByUploadedAtDesc(
	                            customerPolicy);

	    return documents.stream()
	            .map(this::mapToAdminDocumentResponse)
	            .toList();
	}
	
	public AdminDocumentResponse getDocumentByPolicy(
	        Long customerPolicyId,
	        Long documentId) {


	    CustomerPolicy customerPolicy =
	            customerPolicyRepository
	            .findById(customerPolicyId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Customer policy not found"));


	    Document document =
	            documentRepository
	            .findByDocumentIdAndCustomerPolicy(
	                    documentId,
	                    customerPolicy)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Document not found for this policy"));


	    return mapToAdminDocumentResponse(document);
	}
	
	public DocumentDownloadResponse downloadDocument(
	        Long customerPolicyId,
	        Long documentId)
	        throws IOException {


	    CustomerPolicy customerPolicy =
	            customerPolicyRepository
	            .findById(customerPolicyId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Customer policy not found"));


	    Document document =
	            documentRepository
	            .findByDocumentIdAndCustomerPolicy(
	                    documentId,
	                    customerPolicy)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Document not found for this policy"));


	    byte[] fileData =
	            Files.readAllBytes(
	                    Paths.get(document.getFilePath()));


	    DocumentDownloadResponse response =
	            new DocumentDownloadResponse();

	    response.setData(fileData);

	    response.setFileName(
	            document.getOriginalFileName());

	    response.setContentType(
	            document.getContentType());


	    return response;
	}
	
	@Transactional
	public void deleteDocument(
	        Long customerPolicyId,
	        Long documentId) {


	    CustomerPolicy customerPolicy =
	            customerPolicyRepository
	            .findById(customerPolicyId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Customer policy not found"));


	    Document document =
	            documentRepository
	            .findByDocumentIdAndCustomerPolicy(
	                    documentId,
	                    customerPolicy)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Document not found for this policy"));


	    if (document.getStatus() == DocumentStatus.DELETED) {

	        throw new BadRequestException(
	                "Document already deleted");
	    }


	    document.setStatus(DocumentStatus.DELETED);

	    document.setUpdatedAt(
	            LocalDateTime.now());


	    documentRepository.save(document);
	}

}