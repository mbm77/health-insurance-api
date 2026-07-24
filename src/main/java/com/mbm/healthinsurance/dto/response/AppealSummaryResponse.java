package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.AppealCategory;
import com.mbm.healthinsurance.enums.AppealStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppealSummaryResponse {

    private Long appealId;

    private String appealNumber;

    private AppealCategory category;

    private String subject;

    private AppealStatus status;

    private LocalDateTime createdAt;
}