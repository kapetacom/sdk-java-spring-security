/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.security.consumer;

import com.kapeta.spring.security.AuthorizationForwarder;
import com.kapeta.spring.security.AuthorizationForwarderSupplier;
import com.kapeta.spring.security.JWTAuthorizationForwarder;
import com.kapeta.spring.security.JWTSecurityContext;
import com.kapeta.spring.security.provider.JWKInternalKeyStore;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapeta.spring.config.providers.KapetaConfigurationProvider;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class KapetaSecurityConsumerConfig {

    @Bean
    public JWKSConsumer jwkProvider(KapetaConfigurationProvider kapetaConfigurationProvider, ObjectMapper objectMapper) {
        return new JWKSConsumer(kapetaConfigurationProvider, objectMapper);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
                // Default to permit all - add spring security annotations to controllers to restrict access
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder))
                );
        return http.build();
    }



    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder jwtDecoder(JWKSConsumer jwksConsumer) {
        return NimbusJwtDecoder.withJwkSetUri(jwksConsumer.getJwksUri()).build();
    }

    @Bean
    public JWTSecurityContext jwtSecurityService() {
        return new JWTSecurityContext();
    }

    @Bean
    @Primary
    public AuthorizationForwarderSupplier jwtAuthorizationForwarderSupplier(JWTSecurityContext jwtSecurityContext) {
        return () -> new JWTAuthorizationForwarder(jwtSecurityContext);
    }

}
