package com.mbm.healthinsurance.dto.response;

import com.mbm.healthinsurance.enums.CompanyStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long companyId;

    private String companyName;

    private String registrationNumber;

    private String email;

    private String phone;

    private String website;

    private String address;

    private CompanyStatus status;
}