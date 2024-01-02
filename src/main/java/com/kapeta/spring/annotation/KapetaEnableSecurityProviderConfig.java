package com.kapeta.spring.annotation;

import com.kapeta.spring.config.KapetaSecurityProviderConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({KapetaSecurityProviderConfig.class})
public @interface KapetaEnableSecurityProviderConfig {
}
