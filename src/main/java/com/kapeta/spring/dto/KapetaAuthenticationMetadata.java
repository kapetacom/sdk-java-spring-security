package com.kapeta.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KapetaAuthenticationMetadata {
    private String type;
    private String jwks;
    private String issuer;
    private String[] audience;
}