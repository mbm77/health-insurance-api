package com.mbm.healthinsurance.payment.validation;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.enums.CustomerPolicyStatus;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

	private final CustomerRepository customerRepository;

	private final CustomerPolicyRepository customerPolicyRepository;

	private final PaymentRepository paymentRepository;

	public Customer getAuthenticatedCustomer(
			Authentication authentication) {

		String username = authentication.getName();

		return customerRepository
				.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found."));
	}

	public CustomerPolicy validateCustomerPolicy(

			Long customerPolicyId,

			Customer customer) {

		return customerPolicyRepository
				.findByCustomerPolicyIdAndCustomer(
						customerPolicyId,
						customer)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer policy not found."));
	}
	
	

	public void validatePolicyForPurchasePayment(

			CustomerPolicy customerPolicy) {

		if (customerPolicy.getStatus() != CustomerPolicyStatus.PAYMENT_PENDING) {

			throw new BadRequestException(

					"Premium payment is not allowed for policy status: "

							+ customerPolicy.getStatus());
		}
	}
	
	

}