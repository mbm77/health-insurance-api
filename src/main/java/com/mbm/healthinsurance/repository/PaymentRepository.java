package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbm.healthinsurance.entity.Claim;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.Payment;
import com.mbm.healthinsurance.enums.PaymentStatus;
import com.mbm.healthinsurance.enums.PaymentType;

@Repository
public interface PaymentRepository
		extends JpaRepository<Payment, Long> {

	List<Payment> findByCustomerCustomerId(Long customerId);

	List<Payment> findByCustomerPolicyCustomerPolicyId(
			Long customerPolicyId);

	Optional<Payment> findByPaymentIdAndCustomer(
			Long paymentId,
			Customer customer);

	Optional<Payment> findByPaymentId(Long paymentId);

	boolean existsByCustomerPolicyAndPaymentTypeAndStatus(
			CustomerPolicy customerPolicy,
			PaymentType paymentType,
			PaymentStatus status);

	List<Payment> findAllByOrderByCreatedAtDesc();

	long countByCustomer(Customer customer);

	long countByCustomerAndStatus(
			Customer customer,
			PaymentStatus status);

	List<Payment> findTop5ByCustomerOrderByCreatedAtDesc(
			Customer customer);

	long countByStatus(PaymentStatus status);

	
	
	List<Payment> findByCustomerPolicyCustomerPolicyIdOrderByPaymentIdDesc(
	        Long customerPolicyId);
	
	 List<Payment> findByCustomerPolicyOrderByPaymentDateDesc(
	            CustomerPolicy customerPolicy);
	 
	 Optional<Payment> findByCustomerPolicyAndPaymentTypeAndStatus(
		        CustomerPolicy customerPolicy,
		        PaymentType paymentType,
		        PaymentStatus status);
	 
	 Optional<Payment> findByPaymentIdAndCustomerCustomerId(
		        Long paymentId,
		        Long customerId);
	 
	 boolean existsByClaimClaimId(Long claimId);
	 
	
	 
	 Optional<Payment> findByClaimClaimId(Long claimId);
	 Optional<Payment> findByClaim(Claim claim);
	 
	
	

}