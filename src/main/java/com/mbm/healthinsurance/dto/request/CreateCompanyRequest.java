package com.mbm.healthinsurance.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 100)
    private String companyName;

    @NotBlank(message = "Registration number is required")
    @Size(max = 100)
    private String registrationNumber;

    @Email(message = "Invalid email address")
    @Size(max = 100)
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number")
    private String phone;

    @Size(max = 150)
    private String website;

    @NotBlank(message = "Address is required")
    @Size(max = 255)
    private String address;
}