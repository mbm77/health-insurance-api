package com.mbm.healthinsurance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.CreateCompanyRequest;
import com.mbm.healthinsurance.dto.request.UpdateCompanyRequest;
import com.mbm.healthinsurance.dto.request.UpdateCompanyStatusRequest;
import com.mbm.healthinsurance.dto.response.CompanyResponse;
import com.mbm.healthinsurance.dto.response.InsurancePlanResponse;
import com.mbm.healthinsurance.enums.CompanyStatus;
import com.mbm.healthinsurance.enums.PlanStatus;
import com.mbm.healthinsurance.exception.ResourceAlreadyExistsException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.model.InsuranceCompany;
import com.mbm.healthinsurance.model.InsurancePlan;
import com.mbm.healthinsurance.repository.InsuranceCompanyRepository;
import com.mbm.healthinsurance.repository.InsurancePlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsuranceCompanyService {

	private final InsuranceCompanyRepository companyRepository;
    private final InsurancePlanRepository insurancePlanRepository;

	public CompanyResponse createCompany(CreateCompanyRequest request) {

		validateCompany(request);

		InsuranceCompany company = InsuranceCompany.builder().companyName(request.getCompanyName())
				.registrationNumber(request.getRegistrationNumber()).email(request.getEmail()).phone(request.getPhone())
				.website(request.getWebsite()).address(request.getAddress()).status(CompanyStatus.ACTIVE).build();

		company = companyRepository.save(company);

		return CompanyResponse.builder().companyId(company.getCompanyId()).companyName(company.getCompanyName())
				.registrationNumber(company.getRegistrationNumber()).email(company.getEmail()).phone(company.getPhone())
				.website(company.getWebsite()).address(company.getAddress()).status(company.getStatus()).build();
	}

	private void validateCompany(CreateCompanyRequest request) {

		if (companyRepository.existsByCompanyNameIgnoreCase(request.getCompanyName())) {
			throw new ResourceAlreadyExistsException("Company name already exists.");
		}

		if (companyRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
			throw new ResourceAlreadyExistsException("Registration number already exists.");
		}

		if (request.getEmail() != null && companyRepository.existsByEmailIgnoreCase(request.getEmail())) {

			throw new ResourceAlreadyExistsException("Email already exists.");
		}
	}

	public List<CompanyResponse> getAllCompanies() {

		return companyRepository.findByStatus(CompanyStatus.ACTIVE).stream()
				.map(company -> CompanyResponse.builder().companyId(company.getCompanyId())
						.companyName(company.getCompanyName()).registrationNumber(company.getRegistrationNumber())
						.email(company.getEmail()).phone(company.getPhone()).website(company.getWebsite())
						.address(company.getAddress()).status(company.getStatus()).build())
				.toList();
	}
/*
	public CompanyResponse getCompanyById(Long companyId) {

		InsuranceCompany company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance company not found with id: " + companyId));

		return CompanyResponse.builder().companyId(company.getCompanyId()).companyName(company.getCompanyName())
				.registrationNumber(company.getRegistrationNumber()).email(company.getEmail()).phone(company.getPhone())
				.website(company.getWebsite()).address(company.getAddress()).status(company.getStatus()).build();
	}
	*/

	public CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request) {

		InsuranceCompany company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance company not found with id: " + companyId));

		if (company.getStatus() == CompanyStatus.INACTIVE) {
			throw new ResourceAlreadyExistsException("Inactive company cannot be updated. Activate it first.");
		}

// Company Name Validation
		companyRepository.findByCompanyNameIgnoreCase(request.getCompanyName()).ifPresent(existingCompany -> {
			if (!existingCompany.getCompanyId().equals(companyId)) {
				throw new ResourceAlreadyExistsException("Company name already exists.");
			}
		});

// Registration Number Validation
		if (!company.getRegistrationNumber().equals(request.getRegistrationNumber())
				&& companyRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {

			throw new ResourceAlreadyExistsException("Registration number already exists.");
		}

// Email Validation
		if (!company.getEmail().equalsIgnoreCase(request.getEmail())
				&& companyRepository.existsByEmailIgnoreCase(request.getEmail())) {

			throw new ResourceAlreadyExistsException("Email already exists.");
		}

		company.setCompanyName(request.getCompanyName());
		company.setRegistrationNumber(request.getRegistrationNumber());
		company.setEmail(request.getEmail());
		company.setPhone(request.getPhone());
		company.setWebsite(request.getWebsite());
		company.setAddress(request.getAddress());

		InsuranceCompany updatedCompany = companyRepository.save(company);

		return CompanyResponse.builder().companyId(updatedCompany.getCompanyId())
				.companyName(updatedCompany.getCompanyName()).registrationNumber(updatedCompany.getRegistrationNumber())
				.email(updatedCompany.getEmail()).phone(updatedCompany.getPhone()).website(updatedCompany.getWebsite())
				.address(updatedCompany.getAddress()).status(updatedCompany.getStatus()).build();
	}

	public CompanyResponse updateCompanyStatus(Long companyId, UpdateCompanyStatusRequest request) {

		InsuranceCompany company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance company not found with id: " + companyId));

		if (company.getStatus() == request.getStatus()) {
			throw new ResourceAlreadyExistsException("Company is already " + request.getStatus());
		}
		company.setStatus(request.getStatus());

		InsuranceCompany updatedCompany = companyRepository.save(company);

		return CompanyResponse.builder().companyId(updatedCompany.getCompanyId())
				.companyName(updatedCompany.getCompanyName()).registrationNumber(updatedCompany.getRegistrationNumber())
				.email(updatedCompany.getEmail()).phone(updatedCompany.getPhone()).website(updatedCompany.getWebsite())
				.address(updatedCompany.getAddress()).status(updatedCompany.getStatus()).build();
	}

	public void deleteCompany(Long companyId) {

		InsuranceCompany company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance company not found with id: " + companyId));

		if (company.getStatus() == CompanyStatus.INACTIVE) {
			throw new ResourceAlreadyExistsException("Company is already inactive.");
		}

		company.setStatus(CompanyStatus.INACTIVE);

		companyRepository.save(company);
	}
	

   

    public List<CompanyResponse> getAllActiveCompanies() {

        List<InsuranceCompany> companies =
        		companyRepository.findByStatus(CompanyStatus.ACTIVE);

        return companies.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CompanyResponse getCompanyById(Long companyId) {

        InsuranceCompany company = companyRepository
                .findByCompanyIdAndStatus(
                        companyId,
                        CompanyStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + companyId));

        return mapToResponse(company);
    }

    
    public List<InsurancePlanResponse> getPlansByCompany(Long companyId) {

        InsuranceCompany company = companyRepository
                .findByCompanyIdAndStatus(
                        companyId,
                        CompanyStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active company not found with id: " + companyId));

        List<InsurancePlan> plans =
                insurancePlanRepository.findByInsuranceCompanyAndStatus(
                        company,
                        PlanStatus.ACTIVE);

        return plans.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    private InsurancePlanResponse mapToResponse(InsurancePlan plan) {

        return InsurancePlanResponse.builder()
                .planId(plan.getPlanId())
                .companyId(plan.getInsuranceCompany().getCompanyId())
                .companyName(plan.getInsuranceCompany().getCompanyName())
                .planName(plan.getPlanName())
                .description(plan.getDescription())
                .status(plan.getStatus())
                .build();
    }
    
    
    private CompanyResponse mapToResponse(InsuranceCompany company) {

        return CompanyResponse.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .registrationNumber(company.getRegistrationNumber())
                .email(company.getEmail())
                .phone(company.getPhone())
                .website(company.getWebsite())
                .address(company.getAddress())
                .status(company.getStatus())
                .build();
    }
}