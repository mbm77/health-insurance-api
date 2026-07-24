package com.mbm.healthinsurance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDownloadResponse {


    private byte[] data;

    private String fileName;

    private String contentType;
}