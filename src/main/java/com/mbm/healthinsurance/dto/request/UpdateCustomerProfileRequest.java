package com.mbm.healthinsurance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mbm.healthinsurance.enums.BloodGroup;
import com.mbm.healthinsurance.enums.Gender;
import com.mbm.healthinsurance.enums.MaritalStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCustomerProfileRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Pattern(regexp = "\\d{12}", message = "Aadhaar must contain 12 digits")
    private String aadhaarNumber;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN number")
    private String panNumber;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @NotNull(message = "Marital status is required")
    private MaritalStatus maritalStatus;
    
    @NotBlank(message = "Occupation is required")
    private String occupation;

    @NotNull(message = "Annual income is required")
    private BigDecimal annualIncome;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @Pattern(regexp = "\\d{6}", message = "Pincode must contain 6 digits")
    private String pincode;
}