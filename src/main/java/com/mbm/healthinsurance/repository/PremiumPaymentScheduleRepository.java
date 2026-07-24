package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.PremiumPaymentSchedule;
import com.mbm.healthinsurance.enums.PremiumPaymentScheduleStatus;


public interface PremiumPaymentScheduleRepository 
        extends JpaRepository<PremiumPaymentSchedule, Long> {


    List<PremiumPaymentSchedule> findByCustomerPolicy(
            CustomerPolicy customerPolicy);


    List<PremiumPaymentSchedule> findByCustomerPolicyOrderByDueDateAsc(
            CustomerPolicy customerPolicy);


    Optional<PremiumPaymentSchedule> findFirstByCustomerPolicyAndStatusOrderByDueDateAsc(
            CustomerPolicy customerPolicy,
            PremiumPaymentScheduleStatus status);
    
    List<PremiumPaymentSchedule> findByCustomerPolicyCustomerPolicyIdOrderByDueDateAsc(
            Long customerPolicyId);
    
    Optional<PremiumPaymentSchedule>
    findByScheduleIdAndCustomerPolicyCustomerPolicyId(
            Long scheduleId,
            Long customerPolicyId);
    

}