package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private LocalDateTime timestamp;

    private int status;

    private String message;

    private String accessToken;

    private String tokenType;
}