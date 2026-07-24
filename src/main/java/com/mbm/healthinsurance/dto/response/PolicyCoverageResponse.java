package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;

import com.mbm.healthinsurance.enums.CoverageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PolicyCoverageResponse {

	private Long coverageId;

	private Long policyId;

	private String policyName;

	private String coverageName;

	private BigDecimal coverageAmount;

	private String description;
	private CoverageStatus status;
}