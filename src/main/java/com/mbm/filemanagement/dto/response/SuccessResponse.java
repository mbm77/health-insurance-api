package com.mbm.filemanagement.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String fileName;

}