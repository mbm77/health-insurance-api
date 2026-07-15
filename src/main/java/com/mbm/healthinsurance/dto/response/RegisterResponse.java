package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;
}