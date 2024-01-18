/*
 * Copyright 2023 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.function.Supplier;

/**
 * This class is used to get the JWT from the SecurityContext.
 */
public class JWTSecurityContext implements Supplier<Jwt> {

    /**
     * Get the current verified JWT from the SecurityContext.
     */
    public Jwt get() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }

        // if the user is anonymous principal is not Jwt.
        if (authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }

        return null;
    }
}
