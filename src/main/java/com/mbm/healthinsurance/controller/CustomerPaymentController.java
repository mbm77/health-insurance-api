package com.mbm.healthinsurance.controller;


import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.filemanagement.dto.response.CustomerPaymentResponse;
import com.mbm.healthinsurance.dto.response.PremiumHistoryResponse;
import com.mbm.healthinsurance.service.PaymentService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/customers/policies")
@RequiredArgsConstructor
public class CustomerPaymentController {


    private final PaymentService paymentService;



    @GetMapping("/{customerPolicyId}/premium-history")
    public List<PremiumHistoryResponse> getPremiumHistory(
            Authentication authentication,
            @PathVariable Long customerPolicyId) {


        return paymentService.getPremiumHistory(
                authentication,
                customerPolicyId);
    }
    
    @GetMapping("/payments/{paymentId}")
    public CustomerPaymentResponse getPaymentDetails(
            Authentication authentication,
            @PathVariable Long paymentId) {

        return paymentService.getCustomerPaymentDetails(
                authentication,
                paymentId);
    }

}