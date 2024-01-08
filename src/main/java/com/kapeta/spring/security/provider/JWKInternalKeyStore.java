/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.security.provider;

import com.nimbusds.jose.jwk.JWKSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWKInternalKeyStore {
    private String issuer;
    private String audience;
    private JWKSet keyStore;
}
