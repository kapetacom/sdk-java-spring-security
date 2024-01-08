/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.security.provider;

import com.kapeta.spring.security.AuthorizationForwarder;
import com.kapeta.spring.security.AuthorizationForwarderSupplier;
import com.kapeta.spring.security.JWTAuthorizationForwarder;
import com.kapeta.spring.security.JWTSecurityContext;
import com.kapeta.spring.security.provider.rest.KapetaAuthenticationRestController;
import com.kapeta.spring.security.provider.rest.KapetaJwksRestController;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class JWKSProviderConfiguration {

    @Value("${kapeta.security.jwks.issuer:https://example.auth.kapeta.com}")
    private String jwksIssuer;

    @Value("${kapeta.security.jwks.audience:https://example.kapeta.com}")
    private String jwksAudience;

    @Value("${kapeta.security.jwt.ttl:3600000}")
    private long jwtTtl;

    @Bean
    public KapetaAuthenticationRestController kapetaAuthenticationRestController(JWKInternalKeyStore jwkInternalKeyStoreProvider) {
        return new KapetaAuthenticationRestController(jwkInternalKeyStoreProvider);
    }

    @Bean
    public KapetaJwksRestController kapetaJwksRestController(JWKInternalKeyStore jwkInternalKeyStoreProvider) {
        return new KapetaJwksRestController(jwkInternalKeyStoreProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
                // Default to permit all - add spring security annotations to controllers to restrict access
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtCustomizer ->
                                jwtCustomizer.decoder(jwtDecoder)
                        )
                );
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean(JWTSecurityContext.class)
    public JWTSecurityContext jwtSecurityService() {
        return new JWTSecurityContext();
    }

    @Bean
    @ConditionalOnMissingBean(JWTCreatorService.class)
    public JWTCreatorService jwtCreatorService(JWKInternalKeyStore jwkInternalKeyStore) {
        return new JWTCreatorService(jwkInternalKeyStore, jwtTtl);
    }

    @Bean
    @Primary
    public AuthorizationForwarderSupplier jwtAuthorizationForwarderSupplier(JWTSecurityContext jwtSecurityContext) {
        return () -> new JWTAuthorizationForwarder(jwtSecurityContext);
    }

    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder jwtDecoder(JWKInternalKeyStore jwkInternalKeyStore) {
        var jwtProcessor = new DefaultJWTProcessor<>();
        var jwkSource = new ImmutableJWKSet<>(jwkInternalKeyStore.getKeyStore());
        var keySelector = new JWSAlgorithmFamilyJWSKeySelector<>(JWSAlgorithm.Family.SIGNATURE, jwkSource);
        jwtProcessor.setJWSKeySelector(keySelector);
        jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder()
                        .issuer(jwkInternalKeyStore.getIssuer())
                        .audience(jwkInternalKeyStore.getAudience())
                        .build(),
                Set.of(JWTClaimNames.SUBJECT, JWTClaimNames.EXPIRATION_TIME)
        ));

        return new NimbusJwtDecoder(jwtProcessor);
    }

    @Bean
    @ConditionalOnMissingBean(JWKSetProvider.class)
    public JWKSetProvider jwkSetProvider(@Value("${kapeta.security.jwks.file:jwks.json}") String filename) {
        return new FileBasedJWKSetProvider(filename);
    }

    @Bean
    @ConditionalOnMissingBean(JWKInternalKeyStore.class)
    public JWKInternalKeyStore jwkInternalKeyStore(JWKSetProvider jwkSetProvider) {
        return new JWKInternalKeyStore(jwksIssuer, jwksAudience, jwkSetProvider.get());
    }
}
