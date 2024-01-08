/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security.provider;

import com.nimbusds.jose.jwk.JWKSet;

import java.util.function.Supplier;

public interface JWKSetProvider extends Supplier<JWKSet> {

}
