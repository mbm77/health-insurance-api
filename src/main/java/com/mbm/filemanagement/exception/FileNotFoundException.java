package com.mbm.filemanagement.exception;

public class FileNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String message) {
		super(message);
	}

}