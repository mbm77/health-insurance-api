package com.mbm.filemanagement.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mbm.filemanagement.dto.response.FileInfoResponse;
import com.mbm.filemanagement.dto.response.FileListResponse;
import com.mbm.filemanagement.dto.response.MultipleFileUploadResponse;
import com.mbm.filemanagement.dto.response.SuccessResponse;
import com.mbm.filemanagement.exception.EmptyFileException;
import com.mbm.filemanagement.exception.FileAlreadyExistsException;
import com.mbm.filemanagement.exception.FileNotFoundException;
import com.mbm.filemanagement.exception.FileSizeExceededException;
import com.mbm.filemanagement.exception.FileStorageException;
import com.mbm.filemanagement.exception.InvalidFileNameException;
import com.mbm.filemanagement.exception.InvalidFileTypeException;

@Service
public class FileService {

	// Upload folder in the project root
	private static final String UPLOAD_DIR = "uploads";

	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

	private static final Set<String> ALLOWED_EXTENSIONS = Set.of("txt", "pdf", "png");

	public ResponseEntity<SuccessResponse> uploadFile(MultipartFile file) {

		// Empty file validation
		if (file.isEmpty()) {
			throw new EmptyFileException("File is empty");
		}

		String fileName = file.getOriginalFilename();

		// Invalid file name validation
		if (fileName == null || fileName.isBlank() || fileName.contains("..") || fileName.contains("/")
				|| fileName.contains("\\") || fileName.contains("*") || fileName.contains("?")
				|| !fileName.contains(".")) {

			throw new InvalidFileNameException("Invalid file name.");
		}

		// Allowed extension validation
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			throw new InvalidFileTypeException("Only " + ALLOWED_EXTENSIONS + " files are allowed.");
		}

		// Maximum file size validation (Example: 5 MB)
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new FileSizeExceededException("Maximum allowed file size is 5 MB.");
		}

		try {

			Path uploadPath = Paths.get(UPLOAD_DIR);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Path filePath = uploadPath.resolve(fileName);

			// Duplicate file validation
			if (Files.exists(filePath)) {
				throw new FileAlreadyExistsException("File already exists : " + fileName);
			}

			Files.copy(file.getInputStream(), filePath);

			SuccessResponse response = SuccessResponse.builder().timestamp(LocalDateTime.now())
					.status(HttpStatus.OK.value()).message("File uploaded successfully").fileName(fileName).build();

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			throw new FileStorageException("Failed to upload file", e);
		}
	}

	public ResponseEntity<MultipleFileUploadResponse> uploadMultipleFiles(MultipartFile[] files) {

		List<SuccessResponse> uploadedFiles = new ArrayList<>();

		for (MultipartFile file : files) {
			ResponseEntity<SuccessResponse> response = uploadFile(file);
			uploadedFiles.add(response.getBody());
		}

		MultipleFileUploadResponse response = MultipleFileUploadResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.OK.value()).message("Files uploaded successfully").count(uploadedFiles.size())
				.files(uploadedFiles).build();

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Resource> downloadFile(String fileName) {

		try {

			Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

			if (!Files.exists(filePath)) {
				throw new FileNotFoundException("File not found : " + fileName);
			}

			Resource resource = new UrlResource(filePath.toUri());

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (MalformedURLException e) {
			throw new RuntimeException("Unable to download file", e);
		}

	}

	public ResponseEntity<SuccessResponse> deleteFile(String fileName) {

		try {

			Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

			if (!Files.exists(filePath)) {
				throw new FileNotFoundException("File not found : " + fileName);
			}

			Files.delete(filePath);

			SuccessResponse response = SuccessResponse.builder().timestamp(LocalDateTime.now())
					.status(HttpStatus.OK.value()).message("File deleted successfully").fileName(fileName).build();

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			throw new FileStorageException("Failed to delete file", e);
		}
	}

	public ResponseEntity<FileInfoResponse> getFileInfo(String fileName) {

		try {

			Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

			if (!Files.exists(filePath)) {
				throw new FileNotFoundException("File not found : " + fileName);
			}

			FileInfoResponse response = FileInfoResponse.builder().fileName(fileName).size(Files.size(filePath))
					.contentType(Files.probeContentType(filePath)).lastModified(Files.getLastModifiedTime(filePath)
							.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
					.build();

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			throw new FileStorageException("Unable to get file information", e);
		}
	}

	public ResponseEntity<FileListResponse> getAllFiles() {

		try (Stream<Path> paths = Files.list(Paths.get(UPLOAD_DIR))) {
			List<FileInfoResponse> files = paths.map(this::buildFileInfo).toList();

			FileListResponse response = FileListResponse.builder().count(files.size()).status(HttpStatus.OK.value())
					.timestamp(LocalDateTime.now()).message("Files retrieved successfully").files(files).build();

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			throw new FileStorageException("Unable to retrieve files", e);
		}
	}

	public ResponseEntity<SuccessResponse> updateFile(String fileName, MultipartFile file) {

		if (file.isEmpty()) {
			throw new EmptyFileException("File is empty");
		}

		//String fileName = file.getOriginalFilename();

		// Invalid file name validation
		if (fileName == null || fileName.isBlank() || fileName.contains("..") || fileName.contains("/")
				|| fileName.contains("\\") || fileName.contains("*") || fileName.contains("?")
				|| !fileName.contains(".")) {

			throw new InvalidFileNameException("Invalid file name.");
		}

		// Allowed extension validation
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			throw new InvalidFileTypeException("Only " + ALLOWED_EXTENSIONS + " files are allowed.");
		}

		// Maximum file size validation (Example: 5 MB)
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new FileSizeExceededException("Maximum allowed file size is 5 MB.");
		}

		Path uploadPath = Paths.get(UPLOAD_DIR);
		Path filePath = uploadPath.resolve(fileName);

		if (!Files.exists(filePath)) {
			throw new FileNotFoundException("File not found : " + fileName);
		}

		try {

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			SuccessResponse response = SuccessResponse.builder().timestamp(LocalDateTime.now())
					.status(HttpStatus.OK.value()).message("File updated successfully").fileName(fileName).build();

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			throw new FileStorageException("Failed to update file", e);
		}
	}

	private FileInfoResponse buildFileInfo(Path path) {

		try {
			return FileInfoResponse.builder().fileName(path.getFileName().toString()).size(Files.size(path))
					.contentType(Files.probeContentType(path)).lastModified(Files.getLastModifiedTime(path).toInstant()
							.atZone(ZoneId.systemDefault()).toLocalDateTime())
					.build();

		} catch (IOException e) {
			throw new FileStorageException("Unable to read file information: " + path.getFileName(), e);
		}
	}

}
