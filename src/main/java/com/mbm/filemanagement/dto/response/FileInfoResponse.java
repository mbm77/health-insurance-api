package com.mbm.filemanagement.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfoResponse {

    private String fileName;
    private long size;
    private String contentType;
    private LocalDateTime lastModified;

}