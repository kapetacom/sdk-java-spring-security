/*
 * Copyright 2023 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security.provider;

import com.kapeta.spring.security.JWKSException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

/**
 * Service for creating signed JWT tokens
 */
public class JWTCreatorService {

    private final long timeToLive;

    private final JWKInternalKeyStore jwkInternalKeyStore;

    public JWTCreatorService(JWKInternalKeyStore jwkInternalKeyStore, long timeToLive) {
        this.jwkInternalKeyStore = jwkInternalKeyStore;
        this.timeToLive = timeToLive;
    }

    public JWK getCurrentJWK() {
        return jwkInternalKeyStore.getKeyStore().getKeys().get(0);
    }

    public JWSHeader createHeaders(JWK jwk) {
        return new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .keyID(jwk.getKeyID())
                .build();
    }

    public JWTClaimsSet.Builder createClaimsBuilder() {
        return new JWTClaimsSet.Builder()
                .issuer(jwkInternalKeyStore.getIssuer())
                .audience(jwkInternalKeyStore.getAudience())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + timeToLive));
    }

    public SignedJWT createToken(String subject) {
        var key = getCurrentJWK();

        var claims = createClaimsBuilder().subject(subject).build();

        var headers = createHeaders(key);

        return createToken(key, headers, claims);
    }


    public SignedJWT createToken(JWK jwk, JWSHeader headers, JWTClaimsSet.Builder claims) {
        return createToken(jwk, headers, claims.build());
    }

    public SignedJWT createToken(JWK jwk, JWSHeader headers, JWTClaimsSet claims) {
        try {
            JWSSigner signer = new RSASSASigner(jwk.toRSAKey());

            var jwt = new SignedJWT(
                    headers,
                    claims
            );

            jwt.sign(signer);

            return jwt;
        } catch (JOSEException e) {
            throw new JWKSException("Failed to sign token", e);
        }
    }
}
