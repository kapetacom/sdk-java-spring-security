/*
 * Copyright 2023 Kapeta Inc.
 * SPDX-License-Identifier: MIT
 */
package com.kapeta.spring.security.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapeta.spring.config.providers.KapetaConfigurationProvider;
import com.kapeta.spring.security.dto.KapetaAuthenticationMetadata;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class JWKSConsumer {
    private static final String SERVICE_NAME = "authjwtconsumer";
    private static final String SERVICE_PORT = "http";

    public static final String PATH_KAPETA_AUTHENTICATION = "/.kapeta/authentication.json";

    private final KapetaConfigurationProvider kapetaConfigurationProvider;
    private final ObjectMapper objectMapper;

    public JWKSConsumer(KapetaConfigurationProvider kapetaConfigurationProvider, ObjectMapper objectMapper) {
        this.kapetaConfigurationProvider = kapetaConfigurationProvider;
        this.objectMapper = objectMapper;
    }

    public String getJwksUri() {
        String baseUrl = kapetaConfigurationProvider.getServiceAddress(SERVICE_NAME, SERVICE_PORT);
        while (baseUrl != null && baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        String url = baseUrl + PATH_KAPETA_AUTHENTICATION;
        log.info("Fetching authentication metadata from {}", url);
        KapetaAuthenticationMetadata kapetaAuthenticationMetadata = fetchAndUnmarshal(url);
        var jwksUri = baseUrl + kapetaAuthenticationMetadata.getJwks();
        log.info("Resolved JWKS URI {}", jwksUri);
        return jwksUri;
    }

    private KapetaAuthenticationMetadata fetchAndUnmarshal(String url) {
        String authenticationJson = fetch(url);
        try {
            return objectMapper.readValue(authenticationJson, KapetaAuthenticationMetadata.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String fetch(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) response.append(line);
                    return response.toString();
                }
            } else {
                throw new RuntimeException("Unable to fetch KapetaAuthenticationMetadata. Reason=" + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
