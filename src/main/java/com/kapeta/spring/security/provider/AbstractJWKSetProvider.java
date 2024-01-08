/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security.provider;

import com.kapeta.spring.security.JWKSException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for creating a JWKSet provider.
 * Provides methods for creating and reading JWKSets
 */
public abstract class AbstractJWKSetProvider implements JWKSetProvider {

    protected JWKSet readJWKS(String json) {
        try {
            return JWKSet.parse(json);
        } catch (ParseException e) {
            throw new JWKSException("Failed to parse JWKS from JSON", e);
        }
    }

    protected JWKSet readJWKS(Map<String, Object> json) {
        try {
            return JWKSet.parse(json);
        } catch (ParseException e) {
            throw new JWKSException("Failed to parse JWKS from JSON", e);
        }
    }

    protected JWKSet createJWKS() {
        try {
            return new JWKSet(
                    new RSAKeyGenerator(2048)
                            .keyUse(KeyUse.SIGNATURE)
                            .keyID(UUID.randomUUID().toString())
                            .generate()
            );
        } catch (JOSEException e) {
            throw new JWKSException("Failed to create JWKS", e);
        }
    }

}
