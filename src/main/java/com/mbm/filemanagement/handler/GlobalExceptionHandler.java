package com.mbm.filemanagement.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mbm.filemanagement.dto.response.ErrorResponse;
import com.mbm.filemanagement.exception.EmptyFileException;
import com.mbm.filemanagement.exception.FileAlreadyExistsException;
import com.mbm.filemanagement.exception.FileNotFoundException;
import com.mbm.filemanagement.exception.FileStorageException;
import com.mbm.filemanagement.exception.InvalidCredentialsException;
import com.mbm.filemanagement.exception.InvalidFileTypeException;
import com.mbm.filemanagement.exception.UserAlreadyExistsException;
import com.mbm.filemanagement.exception.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FileAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleFileAlreadyExists(FileAlreadyExistsException ex,
			HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value()).error(HttpStatus.CONFLICT.getReasonPhrase())
				.message(ex.getMessage()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	@ExceptionHandler(EmptyFileException.class)
	public ResponseEntity<ErrorResponse> handleEmptyFile(EmptyFileException ex, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
				.error(status.getReasonPhrase()).message(ex.getMessage()).path(request.getRequestURI()).build();

		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(FileStorageException.class)
	public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex,
			HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).message(ex.getMessage())
				.path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleFileNotFound(FileNotFoundException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value()).error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getMessage()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(InvalidFileTypeException.class)
	public ResponseEntity<ErrorResponse> handleInvalidFileType(InvalidFileTypeException ex,
			HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
				.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase()).message(ex.getMessage())
				.path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex,
			HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value()).error(HttpStatus.CONFLICT.getReasonPhrase())
				.message(ex.getMessage()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value()).error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getMessage()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex,
			HttpServletRequest request) {

		ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.value()).error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
				.message(ex.getMessage()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

}
