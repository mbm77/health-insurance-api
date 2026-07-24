package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.CustomerPolicy;
import com.mbm.healthinsurance.entity.Document;
import com.mbm.healthinsurance.enums.DocumentStatus;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByDocumentNumber(String documentNumber);

    List<Document> findByCustomerOrderByUploadedAtDesc(Customer customer);
    
    Optional<Document> findByDocumentIdAndCustomer(
            Long documentId,
            Customer customer);
    List<Document> findAllByOrderByUploadedAtDesc();
    List<Document> findByCustomerPolicyOrderByUploadedAtDesc(
            CustomerPolicy customerPolicy);
    
    Optional<Document> findByDocumentIdAndCustomerPolicy(
            Long documentId,
            CustomerPolicy customerPolicy);
    
    List<Document> findByCustomerPolicyAndStatusNotOrderByUploadedAtDesc(
            CustomerPolicy customerPolicy,
            DocumentStatus status);
    
    
}