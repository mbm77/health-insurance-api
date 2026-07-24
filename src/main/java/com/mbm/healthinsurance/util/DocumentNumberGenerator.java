package com.mbm.healthinsurance.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class DocumentNumberGenerator {

    public String generateDocumentNumber() {
        return "DOC-" + UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

}