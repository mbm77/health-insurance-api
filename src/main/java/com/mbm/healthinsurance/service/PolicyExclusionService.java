package com.mbm.healthinsurance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.CreatePolicyExclusionRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyExclusionRequest;
import com.mbm.healthinsurance.dto.request.UpdatePolicyExclusionStatusRequest;
import com.mbm.healthinsurance.dto.response.PolicyExclusionResponse;
import com.mbm.healthinsurance.enums.ExclusionStatus;
import com.mbm.healthinsurance.enums.PolicyStatus;
import com.mbm.healthinsurance.exception.ResourceAlreadyExistsException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.model.Policy;
import com.mbm.healthinsurance.model.PolicyExclusion;
import com.mbm.healthinsurance.repository.PolicyExclusionRepository;
import com.mbm.healthinsurance.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyExclusionService {
	
	private final PolicyRepository policyRepository;
	private final PolicyExclusionRepository policyExclusionRepository;

	public PolicyExclusionResponse createExclusion(
	        CreatePolicyExclusionRequest request) {

	    Policy policy = policyRepository.findById(request.getPolicyId())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Policy not found with id: " + request.getPolicyId()));

	    if (policy.getStatus() == PolicyStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Cannot add exclusion to an inactive policy.");
	    }

	    if (policyExclusionRepository.existsByPolicyPolicyIdAndExclusionNameIgnoreCase(
	            request.getPolicyId(),
	            request.getExclusionName())) {

	        throw new ResourceAlreadyExistsException(
	                "Exclusion name already exists for this policy.");
	    }

	    PolicyExclusion exclusion = PolicyExclusion.builder()
	            .policy(policy)
	            .exclusionName(request.getExclusionName())
	            .description(request.getDescription())
	            .status(ExclusionStatus.ACTIVE)
	            .build();

	    PolicyExclusion savedExclusion = policyExclusionRepository.save(exclusion);

	    return PolicyExclusionResponse.builder()
	            .exclusionId(savedExclusion.getExclusionId())
	            .policyId(policy.getPolicyId())
	            .policyName(policy.getPolicyName())
	            .exclusionName(savedExclusion.getExclusionName())
	            .description(savedExclusion.getDescription())
	            .status(savedExclusion.getStatus())
	            .build();
	}
	
	public List<PolicyExclusionResponse> getAllExclusions() {

	    List<PolicyExclusion> exclusions =
	            policyExclusionRepository.findByStatus(ExclusionStatus.ACTIVE);

	    return exclusions.stream()
	            .map(exclusion -> PolicyExclusionResponse.builder()
	                    .exclusionId(exclusion.getExclusionId())
	                    .policyId(exclusion.getPolicy().getPolicyId())
	                    .policyName(exclusion.getPolicy().getPolicyName())
	                    .exclusionName(exclusion.getExclusionName())
	                    .description(exclusion.getDescription())
	                    .status(exclusion.getStatus())
	                    .build())
	            .toList();
	}
	
	public PolicyExclusionResponse getExclusionById(Long exclusionId) {

	    PolicyExclusion exclusion = policyExclusionRepository.findById(exclusionId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Exclusion not found with id: " + exclusionId));

	    if (exclusion.getStatus() == ExclusionStatus.INACTIVE) {
	        throw new ResourceNotFoundException(
	                "Exclusion not found with id: " + exclusionId);
	    }

	    return PolicyExclusionResponse.builder()
	            .exclusionId(exclusion.getExclusionId())
	            .policyId(exclusion.getPolicy().getPolicyId())
	            .policyName(exclusion.getPolicy().getPolicyName())
	            .exclusionName(exclusion.getExclusionName())
	            .description(exclusion.getDescription())
	            .status(exclusion.getStatus())
	            .build();
	}
	
	public PolicyExclusionResponse updateExclusion(
	        Long exclusionId,
	        UpdatePolicyExclusionRequest request) {

	    PolicyExclusion exclusion = policyExclusionRepository.findById(exclusionId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Exclusion not found with id: " + exclusionId));

	    if (exclusion.getStatus() == ExclusionStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Inactive exclusion cannot be updated.");
	    }

	    Policy policy = policyRepository.findById(request.getPolicyId())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Policy not found with id: " + request.getPolicyId()));

	    if (policy.getStatus() == PolicyStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Cannot assign an inactive policy.");
	    }

	    if (policyExclusionRepository
	            .existsByPolicyPolicyIdAndExclusionNameIgnoreCaseAndExclusionIdNot(
	                    request.getPolicyId(),
	                    request.getExclusionName(),
	                    exclusionId)) {

	        throw new ResourceAlreadyExistsException(
	                "Exclusion name already exists for this policy.");
	    }

	    exclusion.setPolicy(policy);
	    exclusion.setExclusionName(request.getExclusionName());
	    exclusion.setDescription(request.getDescription());

	    PolicyExclusion updatedExclusion = policyExclusionRepository.save(exclusion);

	    return PolicyExclusionResponse.builder()
	            .exclusionId(updatedExclusion.getExclusionId())
	            .policyId(updatedExclusion.getPolicy().getPolicyId())
	            .policyName(updatedExclusion.getPolicy().getPolicyName())
	            .exclusionName(updatedExclusion.getExclusionName())
	            .description(updatedExclusion.getDescription())
	            .status(updatedExclusion.getStatus())
	            .build();
	}
	
	public PolicyExclusionResponse updateExclusionStatus(
	        Long exclusionId,
	        UpdatePolicyExclusionStatusRequest request) {

	    PolicyExclusion exclusion = policyExclusionRepository.findById(exclusionId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Exclusion not found with id: " + exclusionId));

	    exclusion.setStatus(request.getStatus());

	    PolicyExclusion updatedExclusion = policyExclusionRepository.save(exclusion);

	    return PolicyExclusionResponse.builder()
	            .exclusionId(updatedExclusion.getExclusionId())
	            .policyId(updatedExclusion.getPolicy().getPolicyId())
	            .policyName(updatedExclusion.getPolicy().getPolicyName())
	            .exclusionName(updatedExclusion.getExclusionName())
	            .description(updatedExclusion.getDescription())
	            .status(updatedExclusion.getStatus())
	            .build();
	}
	
	public void deleteExclusion(Long exclusionId) {

	    PolicyExclusion exclusion = policyExclusionRepository.findById(exclusionId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Exclusion not found with id: " + exclusionId));

	    if (exclusion.getStatus() == ExclusionStatus.INACTIVE) {
	        throw new ResourceAlreadyExistsException(
	                "Exclusion is already inactive.");
	    }

	    exclusion.setStatus(ExclusionStatus.INACTIVE);

	    policyExclusionRepository.save(exclusion);
	}
	
	

}