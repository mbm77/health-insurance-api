package com.mbm.healthinsurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Policy;
import com.mbm.healthinsurance.entity.PolicyPaymentRule;

public interface PolicyPaymentRuleRepository
        extends JpaRepository<PolicyPaymentRule, Long> {

    Optional<PolicyPaymentRule> findByPolicy(Policy policy);

    boolean existsByPolicy(Policy policy);

}