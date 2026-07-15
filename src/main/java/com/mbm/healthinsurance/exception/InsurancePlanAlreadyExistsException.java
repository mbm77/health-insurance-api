package com.mbm.healthinsurance.exception;

public class InsurancePlanAlreadyExistsException extends RuntimeException {
	public InsurancePlanAlreadyExistsException(String message) {
		super(message);
	}
}
