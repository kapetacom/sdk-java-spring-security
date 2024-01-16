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
        return authentication == null ? null :(Jwt) authentication.getPrincipal();
    }
}
