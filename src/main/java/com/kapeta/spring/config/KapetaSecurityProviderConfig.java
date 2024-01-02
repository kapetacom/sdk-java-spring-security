/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.config;

import com.kapeta.spring.rest.KapetaAuthenticationRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class KapetaSecurityProviderConfig {

    @Bean
    public SecurityFilterChain kapetaFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(KapetaAuthenticationRestController.PATH_KAPETA_AUTHENTICATION)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(KapetaAuthenticationRestController.PATH_KAPETA_AUTHENTICATION).permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public KapetaAuthenticationRestController kapetaAuthenticationRestController() {
        return new KapetaAuthenticationRestController();
    }
}
