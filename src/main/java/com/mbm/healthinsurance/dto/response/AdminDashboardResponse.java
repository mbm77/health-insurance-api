package com.mbm.healthinsurance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {


    // Customers

    private long totalCustomers;

    private long activeCustomers;

    private long inactiveCustomers;



    // Insurance Company

    private long totalCompanies;



    // Insurance Plans

    private long totalPlans;



    // Policies

    private long totalPolicies;

    private long activePolicies;

    private long inactivePolicies;



    // Customer Policies

    private long totalCustomerPolicies;

    private long activeCustomerPolicies;

    private long expiredCustomerPolicies;



    // Claims

    private long totalClaims;

    private long submittedClaims;

    private long approvedClaims;

    private long rejectedClaims;



    // Payments

    private long totalPayments;

    private long successfulPayments;

    private long pendingPayments;

    private long failedPayments;

    private long refundedPayments;

}