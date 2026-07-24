package com.mbm.healthinsurance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.healthinsurance.dto.request.PremiumPaymentScheduleDetailsResponse;
import com.mbm.healthinsurance.dto.response.PremiumPaymentScheduleResponse;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.PremiumPaymentSchedule;
import com.mbm.healthinsurance.enums.PremiumFrequency;
import com.mbm.healthinsurance.enums.PremiumPaymentScheduleStatus;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.CustomerPolicyRepository;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.PremiumPaymentScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PremiumPaymentScheduleService {

	private final PremiumPaymentScheduleRepository premiumPaymentScheduleRepository;
	private final CustomerPolicyRepository customerPolicyRepository;
	private final CustomerRepository customerRepository;

	@Transactional
	public void generatePaymentSchedules(CustomerPolicy customerPolicy) {

		List<PremiumPaymentSchedule> schedules = new ArrayList<>();

		LocalDate startDate = customerPolicy.getStartDate();

		Integer policyTermInMonths = customerPolicy.getPolicy()
				.getInsurancePlan()
				.getPolicyTermInMonths();

		int numberOfPayments = calculateNumberOfPayments(
				policyTermInMonths,
				customerPolicy.getPremiumFrequency());

		BigDecimal premiumAmount = customerPolicy.getPremiumAmount();

		for (int i = 1; i <= numberOfPayments; i++) {

			LocalDate dueDate = calculateDueDate(
					startDate,
					customerPolicy.getPremiumFrequency(),
					i);

			PremiumPaymentSchedule schedule = PremiumPaymentSchedule.builder()
					.customerPolicy(customerPolicy)
					.dueDate(dueDate)
					.amount(premiumAmount)
					.status(PremiumPaymentScheduleStatus.PENDING)
					.gracePeriodEndDate(
							calculateGracePeriodEndDate(
									dueDate,
									customerPolicy.getGracePeriodInDays()))
					.build();

			schedules.add(schedule);
		}

		premiumPaymentScheduleRepository.saveAll(schedules);
	}

	private int calculateNumberOfPayments(
			Integer policyTermInMonths,
			PremiumFrequency frequency) {

		return switch (frequency) {

		case MONTHLY ->
			policyTermInMonths;

		case QUARTERLY ->
			policyTermInMonths / 3;

		case HALF_YEARLY ->
			policyTermInMonths / 6;

		case YEARLY ->
			policyTermInMonths / 12;
		};
	}

	private LocalDate calculateDueDate(
			LocalDate startDate,
			PremiumFrequency frequency,
			int paymentNumber) {

		return switch (frequency) {

		case MONTHLY ->
			startDate.plusMonths(paymentNumber);

		case QUARTERLY ->
			startDate.plusMonths(paymentNumber * 3L);

		case HALF_YEARLY ->
			startDate.plusMonths(paymentNumber * 6L);

		case YEARLY ->
			startDate.plusYears(paymentNumber);
		};
	}

	private LocalDate calculateGracePeriodEndDate(
			LocalDate dueDate,
			Integer gracePeriodInDays) {

		if (gracePeriodInDays == null) {
			return null;
		}

		return dueDate.plusDays(gracePeriodInDays);
	}

	public List<PremiumPaymentScheduleResponse> getPaymentSchedules(
			Authentication authentication,
			Long customerPolicyId) {

		Customer customer = customerRepository.findByUserUsername(authentication.getName())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found"));

		CustomerPolicy customerPolicy = customerPolicyRepository.findById(customerPolicyId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer policy not found"));

		if (!customerPolicy.getCustomer()
				.getCustomerId()
				.equals(customer.getCustomerId())) {

			throw new BadRequestException(
					"You are not allowed to access this policy");
		}

		List<PremiumPaymentSchedule> schedules = premiumPaymentScheduleRepository
				.findByCustomerPolicyCustomerPolicyIdOrderByDueDateAsc(
						customerPolicyId);

		return schedules.stream()
				.map(schedule -> PremiumPaymentScheduleResponse.builder()

						.scheduleId(
								schedule.getScheduleId())

						.dueDate(
								schedule.getDueDate())

						.amount(
								schedule.getAmount())

						.status(
								schedule.getStatus())

						.paidDate(
								schedule.getPaidDate())

						.gracePeriodEndDate(
								schedule.getGracePeriodEndDate())

						.build())
				.toList();
	}
	
	@Transactional(readOnly = true)
	public PremiumPaymentScheduleDetailsResponse getPaymentSchedule(
	        Authentication authentication,
	        Long customerPolicyId,
	        Long scheduleId) {

	    String username = authentication.getName();

	    Customer customer = customerRepository
	            .findByUserUsername(username)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Customer not found"));

	    CustomerPolicy customerPolicy = customerPolicyRepository
	            .findById(customerPolicyId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Customer policy not found"));

	    // Validate ownership
	    if (!customerPolicy.getCustomer().getCustomerId()
	            .equals(customer.getCustomerId())) {

	        throw new BadRequestException(
	                "Customer policy does not belong to the logged in customer.");
	    }

	    PremiumPaymentSchedule schedule = premiumPaymentScheduleRepository
	            .findById(scheduleId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Payment schedule not found"));

	    // Validate schedule belongs to customer policy
	    if (!schedule.getCustomerPolicy().getCustomerPolicyId()
	            .equals(customerPolicyId)) {

	        throw new BadRequestException(
	                "Payment schedule does not belong to the specified customer policy.");
	    }

	    return PremiumPaymentScheduleDetailsResponse.builder()
	            .scheduleId(schedule.getScheduleId())
	            .customerPolicyId(customerPolicy.getCustomerPolicyId())
	            .policyNumber(customerPolicy.getPolicyNumber())
	            .policyName(customerPolicy.getPolicy().getPolicyName())
	            .dueDate(schedule.getDueDate())
	            .gracePeriodEndDate(schedule.getGracePeriodEndDate())
	            .amount(schedule.getAmount())
	            .status(schedule.getStatus())
	            .paidDate(schedule.getPaidDate())
	            .build();
	}
}