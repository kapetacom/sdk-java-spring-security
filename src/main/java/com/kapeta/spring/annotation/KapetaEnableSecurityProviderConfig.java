/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.annotation;

import com.kapeta.spring.security.provider.JWKSProviderConfiguration;
import com.kapeta.spring.security.OpenAPIConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({JWKSProviderConfiguration.class, OpenAPIConfiguration.class})
public @interface KapetaEnableSecurityProviderConfig {
}
