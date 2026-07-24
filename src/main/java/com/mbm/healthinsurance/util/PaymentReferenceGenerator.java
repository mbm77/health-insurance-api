package com.mbm.healthinsurance.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class PaymentReferenceGenerator {


    public String generateTransactionReference(){

        return "TXN-" +
                UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();
    }


    public String generateRefundReference(){

        return "REF-" +
                UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();
    }

}