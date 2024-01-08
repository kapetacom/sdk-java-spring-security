/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.annotation;

import com.kapeta.spring.security.OpenAPIConfiguration;
import com.kapeta.spring.security.consumer.KapetaSecurityConsumerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({KapetaSecurityConsumerConfig.class, OpenAPIConfiguration.class})
public @interface KapetaEnableSecurityConsumerConfig {
}
