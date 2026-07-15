package com.mbm.healthinsurance.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserResponse {

    private Long userId;

    private String username;

    private String email;

    private String phone;

    private Boolean enabled;

    private String role;
}