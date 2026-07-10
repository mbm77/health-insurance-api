package com.mbm.filemanagement.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MultipleFileUploadResponse {

	private LocalDateTime timestamp;
	private int status;
	private String message;
	private int count;
	private List<SuccessResponse> files;

}