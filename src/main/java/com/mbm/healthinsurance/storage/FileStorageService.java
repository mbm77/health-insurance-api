package com.mbm.healthinsurance.storage;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	String storeFile(
			MultipartFile file,
			String folderName,
			String storedFileName) throws IOException;

	Resource loadFile(String filePath);

	void deleteFile(String filePath) throws IOException;
	
	byte[] downloadFile(String filePath) throws IOException;
}
