/*
 * Copyright 2024 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security.provider;

import com.kapeta.spring.security.JWKSException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.UUID;

/**
 * Simple file based JWKSet provider.
 *
 * Will create a new JWKSet if the file does not exist and otherwise load it from disk.
 *
 * Use {@link AbstractJWKSetProvider} to create a custom implementation.
 */
public class FileBasedJWKSetProvider extends AbstractJWKSetProvider {

    private final String fileName;

    public FileBasedJWKSetProvider(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public JWKSet get() {
        return new File(fileName).exists()
                ? readJWKSFromFile(fileName)
                : createAndWriteJWKS(fileName);
    }

    private JWKSet readJWKSFromFile(String fileName) {
        try {
            return readJWKS(Files.readString(Paths.get(fileName)));
        } catch (IOException e) {
            throw new JWKSException("Failed to read JWKS from file: %s".formatted(fileName), e);
        }
    }

    private JWKSet createAndWriteJWKS(String fileName) {
        JWKSet jwks = createJWKS();
        writeJWKS(fileName, jwks);
        return jwks;
    }

    private void writeJWKS(String fileName, JWKSet jwkSet) {
        try {
            Files.writeString(Paths.get(fileName), jwkSet.toString(false));
        } catch (IOException e) {
            throw new JWKSException("Failed to write JWKS to file: %s".formatted(fileName), e);
        }
    }
}
