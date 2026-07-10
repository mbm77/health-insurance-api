package com.mbm.filemanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.http.HttpMethod;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    	return http
    	        .csrf(csrf -> csrf.disable())

    	        .sessionManagement(session ->
    	                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

    	        .exceptionHandling(exception -> exception
    	                .authenticationEntryPoint(authenticationEntryPoint)
    	                .accessDeniedHandler(accessDeniedHandler))

    	        .authorizeHttpRequests(auth -> auth

    	                .requestMatchers("/auth/**").permitAll()

    	                .requestMatchers(HttpMethod.DELETE, "/files/**")
    	                .hasRole("ADMIN")

    	                .requestMatchers("/files/**")
    	                .hasAnyRole("USER", "ADMIN")

    	                .anyRequest().authenticated())

    	        .addFilterBefore(jwtAuthenticationFilter,
    	                UsernamePasswordAuthenticationFilter.class)

    	        .build();
    }
}