package com.mbm.healthinsurance.repository;

import com.mbm.healthinsurance.enums.CustomerPolicyStatus;

public interface CustomerPolicyRepsitory {
	boolean existsByCustomerCustomerIdAndPolicyPolicyIdAndStatus(Long customerId, Long policyId,
			CustomerPolicyStatus status);

}
