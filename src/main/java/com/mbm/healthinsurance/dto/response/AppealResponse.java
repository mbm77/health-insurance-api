package com.mbm.healthinsurance.dto.response;

import com.mbm.healthinsurance.enums.AppealCategory;
import com.mbm.healthinsurance.enums.AppealStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppealResponse {

    private Long appealId;

    private String appealNumber;

    private AppealCategory category;

    private AppealStatus status;

    private String message;
}