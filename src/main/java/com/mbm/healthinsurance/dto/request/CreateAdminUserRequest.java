package com.mbm.healthinsurance.dto.request;

import com.mbm.healthinsurance.enums.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class CreateAdminUserRequest {

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 50)
	private String username;

	@NotBlank(message = "Password is required")
	@Size(min = 8)
	private String password;

	@NotBlank(message = "Email is required")
	@Email
	private String email;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
	private String phone;

	@NotNull(message = "Role is required")
	private RoleName role;
}