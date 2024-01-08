/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.security.provider.rest;

import com.kapeta.spring.security.provider.JWKInternalKeyStore;
import com.kapeta.spring.security.dto.KapetaAuthenticationMetadata;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Hidden
public class KapetaAuthenticationRestController {

    public static final String PATH_KAPETA_AUTHENTICATION = "/.kapeta/authentication.json";

    private final JWKInternalKeyStore jwkInternalKeyStore;

    public KapetaAuthenticationRestController(JWKInternalKeyStore jwkInternalKeyStore) {
        this.jwkInternalKeyStore = jwkInternalKeyStore;
        log.info("Initializing Kapeta Authentication Rest Controller");
    }

    @GetMapping(PATH_KAPETA_AUTHENTICATION)
    public KapetaAuthenticationMetadata kapetaAuthenticationMetadata() {
        return new KapetaAuthenticationMetadata("jwt", KapetaJwksRestController.PATH_WELL_KNOWN_JWKS, jwkInternalKeyStore.getIssuer(),
                jwkInternalKeyStore.getAudience() != null ? new String[] {jwkInternalKeyStore.getAudience() } : null);
    }
}
