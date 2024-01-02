/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */

package com.kapeta.spring.annotation;

import com.kapeta.spring.config.KapetaSecurityConsumerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({KapetaSecurityConsumerConfig.class})
public @interface KapetaEnableSecurityConsumerConfig {
}
