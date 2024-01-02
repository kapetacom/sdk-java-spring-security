package com.kapeta.spring.config;

import com.nimbusds.jose.jwk.JWKSet;

import java.util.function.Supplier;

public interface JWKPublicKeySetProvider extends Supplier<JWKSet> {
}
