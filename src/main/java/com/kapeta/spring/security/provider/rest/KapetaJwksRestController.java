/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.security.provider.rest;

import com.kapeta.spring.security.provider.JWKInternalKeyStore;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@Hidden
public class KapetaJwksRestController {
    public static final String PATH_WELL_KNOWN_JWKS = "/.well-known/jwks.json";
    private final JWKInternalKeyStore jwkInternalKeyStore;

    public KapetaJwksRestController(JWKInternalKeyStore jwkInternalKeyStore) {
        this.jwkInternalKeyStore = jwkInternalKeyStore;
        log.debug("Initializing Kapeta JWKS Rest Controller");
    }

    @GetMapping(PATH_WELL_KNOWN_JWKS)
    public Map<String, Object> getJWKS() {
        return jwkInternalKeyStore.getKeyStore().toJSONObject(true);
    }
}
