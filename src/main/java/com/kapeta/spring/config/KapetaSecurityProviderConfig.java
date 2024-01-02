/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.config;

import com.kapeta.spring.rest.KapetaAuthenticationRestController;
import com.kapeta.spring.rest.KapetaJwksRestController;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
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
    public SecurityFilterChain jwksFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(KapetaJwksRestController.PATH_WELL_KNOWN_JWKS)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(KapetaJwksRestController.PATH_WELL_KNOWN_JWKS).permitAll()
                );

        return http.build();
    }

    @Bean
    public KapetaAuthenticationRestController kapetaAuthenticationRestController(@Value("${kapeta.authentication.issuer:#{null}}") String issuer,
                                                                                 @Value("${kapeta.authentication.audience:#{null}}") String audience) {
        return new KapetaAuthenticationRestController(issuer, audience);
    }

    @Bean
    public KapetaJwksRestController kapetaJwksRestController(JWKPublicKeySetProvider jwkPublicKeySetProvider) {
        return new KapetaJwksRestController(jwkPublicKeySetProvider);
    }

    @Bean
    @ConditionalOnMissingBean(JWKPublicKeySetProvider.class)
    public JWKPublicKeySetProvider jwksPublicKeyProvider() {
        log.info("Creating JWKS Public Key");
        try {
            RSAKey jwk = new RSAKeyGenerator(2048)
                    .keyUse(KeyUse.SIGNATURE)
                    .keyID(UUID.randomUUID().toString())
                    .generate().toPublicJWK();
            return () -> new JWKSet(jwk);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
