package com.mbm.healthinsurance.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbm.healthinsurance.dto.request.CreateAdminUserRequest;
import com.mbm.healthinsurance.dto.request.UpdateAdminUserPasswordRequest;
import com.mbm.healthinsurance.dto.request.UpdateAdminUserRequest;
import com.mbm.healthinsurance.dto.request.UpdateAdminUserStatusRequest;
import com.mbm.healthinsurance.dto.response.AdminUserResponse;
import com.mbm.healthinsurance.exception.BadRequestException;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.exception.UserAlreadyExistsException;
import com.mbm.healthinsurance.model.Role;
import com.mbm.healthinsurance.model.User;
import com.mbm.healthinsurance.repository.RoleRepository;
import com.mbm.healthinsurance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
   

    public AdminUserResponse createUser(CreateAdminUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        Role role = roleRepository.findByRoleName(request.getRole().name().toUpperCase())
                .orElseThrow(() ->
                        new ResourceNotFoundException(request.getRole().name() + " role not found"));

        // Prevent CUSTOMER creation through Admin API
        if ("CUSTOMER".equalsIgnoreCase(request.getRole().name())) {
            throw new BadRequestException(
                    "Customer cannot be created using Admin User Management API");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .enabled(true)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);

        return AdminUserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .enabled(user.isEnabled())
                .role(role.getRoleName())
                .build();
    }
    
    /*
    public List<AdminUserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role ->
                                role.getRoleName().equals("ADMIN") ||
                                role.getRoleName().equals("AGENT") ||
                                role.getRoleName().equals("HOSPITAL")))
                .map(user -> {

                    String roleName = user.getRoles()
                            .stream()
                            .findFirst()
                            .map(Role::getRoleName)
                            .orElse("");

                    return AdminUserResponse.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .phone(user.getPhone())
                            .enabled(user.isEnabled())
                            .role(roleName)
                            .build();
                })
                .toList();
    }
    */
    public List<AdminUserResponse> getAllUsers() {

        List<User> users = userRepository.findByRolesRoleNameIn(
                List.of("ADMIN", "AGENT", "HOSPITAL"));

        return users.stream()
                .map(user -> AdminUserResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .enabled(user.isEnabled())
                        .role(user.getRoles().iterator().next().getRoleName())
                        .build())
                .toList();
    }
    
    public AdminUserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        Role role = user.getRoles()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not assigned to user"));

        if (!role.getRoleName().equals("ADMIN")
                && !role.getRoleName().equals("AGENT")
                && !role.getRoleName().equals("HOSPITAL")) {

            throw new ResourceNotFoundException("Admin user not found with id: " + userId);
        }

        return AdminUserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .enabled(user.isEnabled())
                .role(role.getRoleName())
                .build();
    }
    
    public AdminUserResponse updateUser(
            Long userId,
            UpdateAdminUserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        Role role = user.getRoles()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not assigned to user"));

        if (!role.getRoleName().equals("ADMIN")
                && !role.getRoleName().equals("AGENT")
                && !role.getRoleName().equals("HOSPITAL")) {

            throw new ResourceNotFoundException("Admin user not found with id: " + userId);
        }

        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {

            throw new UserAlreadyExistsException("Username already exists");
        }

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new UserAlreadyExistsException("Email already exists");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        return AdminUserResponse.builder()
                .userId(updatedUser.getUserId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .phone(updatedUser.getPhone())
                .enabled(updatedUser.isEnabled())
                .role(role.getRoleName())
                .build();
    }
    
    public AdminUserResponse updateUserStatus(
            Long userId,
            UpdateAdminUserStatusRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        Role role = user.getRoles()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not assigned to user"));

        if (!role.getRoleName().equals("ADMIN")
                && !role.getRoleName().equals("AGENT")
                && !role.getRoleName().equals("HOSPITAL")) {

            throw new ResourceNotFoundException(
                    "Admin user not found with id: " + userId);
        }

        user.setEnabled(request.getEnabled());

        User updatedUser = userRepository.save(user);

        return AdminUserResponse.builder()
                .userId(updatedUser.getUserId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .phone(updatedUser.getPhone())
                .enabled(updatedUser.isEnabled())
                .role(role.getRoleName())
                .build();
    }
    
    
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        Role role = user.getRoles()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not assigned to user"));

        if (!role.getRoleName().equals("ADMIN")
                && !role.getRoleName().equals("AGENT")
                && !role.getRoleName().equals("HOSPITAL")) {

            throw new ResourceNotFoundException(
                    "Admin user not found with id: " + userId);
        }

        userRepository.delete(user);
    }
    
    public AdminUserResponse updateUserPassword(
            Long userId,
            UpdateAdminUserPasswordRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        Role role = user.getRoles()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not assigned to user"));

        if (!role.getRoleName().equals("ADMIN")
                && !role.getRoleName().equals("AGENT")
                && !role.getRoleName().equals("HOSPITAL")) {

            throw new ResourceNotFoundException(
                    "Admin user not found with id: " + userId);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User updatedUser = userRepository.save(user);

        return AdminUserResponse.builder()
                .userId(updatedUser.getUserId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .phone(updatedUser.getPhone())
                .enabled(updatedUser.isEnabled())
                .role(role.getRoleName())
                .build();
    }
    
   
    
    
}
