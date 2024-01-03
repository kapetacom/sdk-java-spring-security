/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.config;

import java.util.function.Supplier;

public interface JWKInternalKeyStoreProvider extends Supplier<JWKInternalKeyStore> {
}
