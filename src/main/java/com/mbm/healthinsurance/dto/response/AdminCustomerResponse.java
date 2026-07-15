package com.mbm.healthinsurance.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mbm.healthinsurance.enums.BloodGroup;
import com.mbm.healthinsurance.enums.Gender;
import com.mbm.healthinsurance.enums.MaritalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCustomerResponse {

    private Long customerId;
    private Long userId;

    private String username;
    private String email;
    private String phone;
    private boolean enabled;

    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String aadhaarNumber;
    private String panNumber;

    private BloodGroup bloodGroup;
    private MaritalStatus maritalStatus;
    private String occupation;
    private BigDecimal annualIncome;

    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
}
