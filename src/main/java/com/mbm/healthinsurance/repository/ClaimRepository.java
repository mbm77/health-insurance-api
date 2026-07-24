package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbm.healthinsurance.entity.Claim;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.Payment;
import com.mbm.healthinsurance.enums.ClaimStatus;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByCustomerPolicy(CustomerPolicy customerPolicy);

    Optional<Claim> findByClaimIdAndCustomerPolicy(
            Long claimId,
            CustomerPolicy customerPolicy);
    
    List<Claim> findByCustomerPolicyCustomer(Customer customer);


    Optional<Claim> findByClaimIdAndCustomerPolicyCustomer(
            Long claimId,
            Customer customer);
    
    long countByCustomerPolicyCustomer(Customer customer);

    long countByCustomerPolicyCustomerAndStatus(
            Customer customer,
            ClaimStatus status);
    
    List<Claim> findTop5ByCustomerPolicyCustomerOrderByCreatedAtDesc(
            Customer customer);
    
    long countByStatus(ClaimStatus status);
    
    Optional<Claim> findByClaimIdAndCustomerPolicyCustomerCustomerId(
            Long claimId,
            Long customerId);

    Optional<Claim> findByClaimId(Long claimId);

    
    
    
    

    //Optional<Claim> findTopByOrderByClaimIdDesc();

}