package com.mbm.healthinsurance.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService implements FileStorageService {

	private final Path rootLocation;

	public LocalFileStorageService(
			@Value("${file.upload-dir}") String uploadDir) {

		this.rootLocation = Paths.get(uploadDir)
				.toAbsolutePath()
				.normalize();

		try {

			Files.createDirectories(this.rootLocation);

		} catch (IOException e) {

			throw new RuntimeException(
					"Could not initialize storage location", e);
		}
	}

	@Override
	public String storeFile(
			MultipartFile file,
			String folderName,
			String storedFileName) throws IOException {

		Path folderPath = rootLocation.resolve(folderName);

		Files.createDirectories(folderPath);

		Path targetLocation = folderPath.resolve(storedFileName);

		Files.copy(
				file.getInputStream(),
				targetLocation);

		return targetLocation.toString();
	}

	@Override
	public Resource loadFile(String filePath) {

		try {

			Path path = Paths.get(filePath);

			Resource resource = new UrlResource(path.toUri());

			if (resource.exists() && resource.isReadable()) {

				return resource;
			}

			throw new RuntimeException(
					"File not found");

		} catch (Exception e) {

			throw new RuntimeException(
					"Could not load file", e);
		}
	}

	@Override
	public void deleteFile(String filePath)
			throws IOException {

		Files.deleteIfExists(
				Paths.get(filePath));
	}

	public byte[] downloadFile(String filePath)
			throws IOException {

		Path path = Paths.get(filePath);

		if (!Files.exists(path)) {

			throw new RuntimeException(
					"File not found");
		}

		return Files.readAllBytes(path);
	}
}