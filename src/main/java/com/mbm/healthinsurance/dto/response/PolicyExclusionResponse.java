package com.mbm.healthinsurance.dto.response;

import com.mbm.healthinsurance.enums.ExclusionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PolicyExclusionResponse {

	private Long exclusionId;

	private Long policyId;

	private String policyName;

	private String exclusionName;

	private String description;

	private ExclusionStatus status;

}