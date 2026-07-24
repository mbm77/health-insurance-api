package com.mbm.healthinsurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.PolicyDocumentRequest;

public interface PolicyDocumentRequestRepository
		extends JpaRepository<PolicyDocumentRequest, Long> {

}