/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ComponentScan("com.kapeta.spring.rest")
public class KapetaSecurityProviderConfig {

    @Bean
    public SecurityFilterChain kapetaFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/.kapeta/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/.kapeta/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
