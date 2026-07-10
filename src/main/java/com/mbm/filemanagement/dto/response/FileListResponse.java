package com.mbm.filemanagement.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileListResponse {

	private LocalDateTime timestamp;
	private String message;
	private int status;
	private int count;
	private List<FileInfoResponse> files;

}