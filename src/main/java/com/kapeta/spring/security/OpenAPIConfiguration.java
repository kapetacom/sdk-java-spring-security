/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        security = @SecurityRequirement(
                name = "Bearer Authentication"
        )
)
@Configuration
@ConditionalOnProperty(name = "kapeta.security.docs.enabled", havingValue = "true", matchIfMissing = true)
public class OpenAPIConfiguration {

}
