/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.rest;

import com.kapeta.spring.dto.KapetaAuthenticationMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class KapetaAuthenticationRestController {

    public static final String PATH_KAPETA_AUTHENTICATION = "/.kapeta/authentication.json";
    private final String issuer;
    private final String audience;

    public KapetaAuthenticationRestController(@Value("${kapeta.authentication.issuer:#{null}}") String issuer,
                                              @Value("${kapeta.authentication.audience:#{null}}") String audience) {
        this.issuer = issuer;
        this.audience = audience;
        log.info("Initializing Kapeta Authentication Rest Controller issuer=[{}] audience=[{}]", issuer, audience);
    }

    @GetMapping(PATH_KAPETA_AUTHENTICATION)
    public KapetaAuthenticationMetadata kapetaAuthenticationMetadata() {
        return new KapetaAuthenticationMetadata("jwt", KapetaJwksRestController.PATH_WELL_KNOWN_JWKS, issuer, audience != null ? new String[] { audience } : null);
    }
}
