package com.mbm.healthinsurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.enums.CompanyStatus;
import com.mbm.healthinsurance.model.InsuranceCompany;

public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long> {

	boolean existsByCompanyNameIgnoreCase(String companyName);

	boolean existsByRegistrationNumber(String registrationNumber);

	boolean existsByEmailIgnoreCase(String email);

	Optional<InsuranceCompany> findByCompanyNameIgnoreCase(String companyName);

	List<InsuranceCompany> findByStatus(CompanyStatus status);

	boolean existsByCompanyNameIgnoreCaseAndStatus(String companyName, CompanyStatus status);

	Optional<InsuranceCompany> findByCompanyIdAndStatus(Long companyId, CompanyStatus status);

}