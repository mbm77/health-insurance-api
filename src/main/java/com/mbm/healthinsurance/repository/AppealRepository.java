package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Appeal;

public interface AppealRepository extends JpaRepository<Appeal, Long> {

    List<Appeal> findByCustomerCustomerIdOrderByCreatedAtDesc(Long customerId);

    boolean existsByAppealNumber(String appealNumber);
    
    Optional<Appeal> findByAppealIdAndCustomerCustomerId(Long appealId, Long customerId);
    
    
    List<Appeal> findAllByOrderByCreatedAtDesc();

}