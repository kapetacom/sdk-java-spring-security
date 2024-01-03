/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.rest;

import com.kapeta.spring.config.JWKInternalKeyStore;
import com.kapeta.spring.config.JWKInternalKeyStoreProvider;
import com.kapeta.spring.dto.KapetaAuthenticationMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class KapetaAuthenticationRestController {

    public static final String PATH_KAPETA_AUTHENTICATION = "/.kapeta/authentication.json";

    private final JWKInternalKeyStoreProvider jwkInternalKeyStoreProvider;

    public KapetaAuthenticationRestController(JWKInternalKeyStoreProvider jwkInternalKeyStoreProvider) {
        this.jwkInternalKeyStoreProvider = jwkInternalKeyStoreProvider;
        log.info("Initializing Kapeta Authentication Rest Controller");
    }

    @GetMapping(PATH_KAPETA_AUTHENTICATION)
    public KapetaAuthenticationMetadata kapetaAuthenticationMetadata() {
        JWKInternalKeyStore jwkInternalKeyStore = jwkInternalKeyStoreProvider.get();
        return new KapetaAuthenticationMetadata("jwt", KapetaJwksRestController.PATH_WELL_KNOWN_JWKS, jwkInternalKeyStore.getIssuer(),
                jwkInternalKeyStore.getAudience() != null ? new String[] {jwkInternalKeyStore.getAudience() } : null);
    }
}
