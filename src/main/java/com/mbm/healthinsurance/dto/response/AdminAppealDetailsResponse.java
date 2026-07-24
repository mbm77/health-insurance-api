package com.mbm.healthinsurance.dto.response;

import java.time.LocalDateTime;

import com.mbm.healthinsurance.enums.AppealCategory;
import com.mbm.healthinsurance.enums.AppealStatus;
import com.mbm.healthinsurance.enums.ReferenceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAppealDetailsResponse {

	private Long appealId;

	private String appealNumber;

	private Long customerId;

	private String customerName;

	private AppealCategory category;

	private String subject;

	private String description;

	private ReferenceType referenceType;

	private Long referenceId;

	private AppealStatus status;

	private String adminRemarks;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
