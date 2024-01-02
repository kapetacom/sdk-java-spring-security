package com.kapeta.spring.rest;

import com.kapeta.spring.dto.KapetaAuthenticationMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class KapetaAuthenticationRestController {

    public static final String PATH_KAPETA_AUTHENTICATION = "/.kapeta/authentication.json";
    public static final String PATH_WELL_KNOWN_JWKS = "/.well-known/jwks.json";

    public KapetaAuthenticationRestController() {
        log.debug("Initializing Kapeta Authentication Rest Controller");
    }

    @GetMapping(PATH_KAPETA_AUTHENTICATION)
    public KapetaAuthenticationMetadata kapetaAuthenticationMetadata() {
        // todo: issuer and audience from config
        return new KapetaAuthenticationMetadata("jwt", PATH_WELL_KNOWN_JWKS, "issuer", new String[] { "audience" });
    }
}
