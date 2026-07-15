package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();
	private int status;
	private String error;
	private String message;
	private String path;
}