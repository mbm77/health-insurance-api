package com.mbm.healthinsurance.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerProfileResponse {
	
	private Long customerId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
}
