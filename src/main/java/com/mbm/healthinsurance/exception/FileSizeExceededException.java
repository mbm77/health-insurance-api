package com.mbm.healthinsurance.exception;

import lombok.Getter;

@Getter
public class FileSizeExceededException extends RuntimeException {


    private final String fileName;
    private final long currentSize;
    private final long allowedSize;


    public FileSizeExceededException(
            String fileName,
            long currentSize,
            long allowedSize) {

        super("File size exceeds allowed limit");

        this.fileName = fileName;
        this.currentSize = currentSize;
        this.allowedSize = allowedSize;
    }
}