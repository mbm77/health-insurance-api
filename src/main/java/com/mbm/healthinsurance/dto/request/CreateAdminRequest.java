package com.mbm.healthinsurance.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAdminRequest {

    private String username;

    private String password;

    private String email;

    private String phone;
}