/*
 * Copyright 2023 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security;

public class JWTAuthorizationForwarder implements AuthorizationForwarder {

    private final JWTSecurityContext jwtSecurityContext;

    public JWTAuthorizationForwarder(JWTSecurityContext jwtSecurityContext) {
        this.jwtSecurityContext = jwtSecurityContext;
    }

    @Override
    public String getAuthorizationValue() {
        var jwt = jwtSecurityContext.get();
        if (jwt == null) {
            return null;
        }
        return "Bearer %s".formatted(jwt.getTokenValue());
    }
}
