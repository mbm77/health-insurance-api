package com.mbm.filemanagement.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mbm.filemanagement.dto.response.FileInfoResponse;
import com.mbm.filemanagement.dto.response.FileListResponse;
import com.mbm.filemanagement.dto.response.MultipleFileUploadResponse;
import com.mbm.filemanagement.dto.response.SuccessResponse;
import com.mbm.filemanagement.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/upload")
	public ResponseEntity<SuccessResponse> uploadFile(@RequestParam("file") MultipartFile file) {

		return fileService.uploadFile(file);

	}

	@PostMapping("/upload-multiple")
	public ResponseEntity<MultipleFileUploadResponse> uploadMultipleFiles(
			@RequestParam("files") MultipartFile[] files) {

		return fileService.uploadMultipleFiles(files);
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {

		return fileService.downloadFile(fileName);
	}

	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<SuccessResponse> deleteFile(@PathVariable String fileName) {

		return fileService.deleteFile(fileName);
	}
	
	@GetMapping("/info/{fileName}")
	public ResponseEntity<FileInfoResponse> getFileInfo(
	        @PathVariable String fileName) {

	    return fileService.getFileInfo(fileName);
	}
	
	@GetMapping
	public ResponseEntity<FileListResponse> getAllFiles() {

	    return fileService.getAllFiles();
	}
	
	@PutMapping("/{fileName}")
	public ResponseEntity<SuccessResponse> updateFile(
	        @PathVariable String fileName,
	        @RequestParam("file") MultipartFile file) {

	    return fileService.updateFile(fileName, file);
	}
}
