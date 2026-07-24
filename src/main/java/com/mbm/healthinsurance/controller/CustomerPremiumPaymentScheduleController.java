package com.mbm.healthinsurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbm.healthinsurance.dto.request.CreatePaymentRequest;
import com.mbm.healthinsurance.dto.request.PremiumPaymentScheduleDetailsResponse;
import com.mbm.healthinsurance.dto.response.PaymentResponse;
import com.mbm.healthinsurance.dto.response.PremiumPaymentScheduleResponse;
import com.mbm.healthinsurance.service.PremiumPaymentScheduleService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/customers/policies")
@RequiredArgsConstructor
public class CustomerPremiumPaymentScheduleController {


    private final PremiumPaymentScheduleService premiumPaymentScheduleService;


    @GetMapping("/{customerPolicyId}/payment-schedules")
    public List<PremiumPaymentScheduleResponse> getPaymentSchedules(
            Authentication authentication,
            @PathVariable Long customerPolicyId) {


        return premiumPaymentScheduleService
                .getPaymentSchedules(
                        authentication,
                        customerPolicyId
                );
    }
    
    @GetMapping("/{customerPolicyId}/payment-schedules/{scheduleId}")
    public ResponseEntity<PremiumPaymentScheduleDetailsResponse> getPaymentSchedule(
            Authentication authentication,
            @PathVariable Long customerPolicyId,
            @PathVariable Long scheduleId) {

        return ResponseEntity.ok(
                premiumPaymentScheduleService.getPaymentSchedule(
                        authentication,
                        customerPolicyId,
                        scheduleId));
    }
    
    /*
    
 
    @PostMapping("/{scheduleId}/pay")
    public ResponseEntity<PaymentResponse> payPremium(
            Authentication authentication,
            @PathVariable Long scheduleId,
            @RequestBody CreatePaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.createRenewalPayment(
                        authentication,
                        scheduleId,
                        request));
    } 
*/
}