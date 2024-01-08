# Kapeta Spring Security SDK

Java SDK using Spring Boot Security for adding security to projects with Kapeta Runtime

## Usage
Usage is split into provider and consumer. 

Providers are responsible for issuing and verifying JWT tokens.
Consumers are responsible for verifying JWT tokens against a provider.

### OpenAPI Documentation

The library provides certain defaults for the OpenAPI configuration which is configured in
[OpenAPIConfiguration](src/main/java/com/kapeta/spring/security/OpenAPIConfiguration.java)

You can disable the OpenAPI configuration by setting `kapeta.security.docs.enabled` to `false`

### Authentication
The SDK will automatically add a filter to the Spring Security filter chain that will verify the JWT token.

You can then use the spring security annotations to secure your endpoints - e.g.:
    
```java
@PreAuthorize("isFullyAuthenticated()")
@GetMapping("/admin")
public String admin() {
    return "Hello Admin";
}
```

To get the currently authenticated JWT token, you can use the `@AuthenticationPrincipal` annotation:

```java
@GetMapping("/user")
@PreAuthorize("isFullyAuthenticated()")
public String user(@AuthenticationPrincipal Jwt jwt) {
    return "Hello " + jwt.getClaimAsString("name");
}
``` 

or you can use the [JWTSecurityContext](src/main/java/com/kapeta/spring/security/JWTSecurityContext.java) :
    
```java

private final JWTSecurityContext jwtSecurityContext;

@GetMapping("/user")
@PreAuthorize("isFullyAuthenticated()")
public String user() {
    return "Hello " + jwtSecurityContext.get().getClaimAsString("name");
}
```

You'll find that at [JWTCreatorService](src/main/java/com/kapeta/spring/security/provider/JWTCreatorService.java)


### Provider
The provider will auto-configure a few things that can be overridden by the user:

The JWT / JWKS provider itself is configured in 
[JWKSProviderConfiguration](src/main/java/com/kapeta/spring/security/provider/JWKSProviderConfiguration.java)

Read that to see what can be overridden and how it works

#### Creating signed JWT tokens
The SDK provides a utility class for creating signed JWT tokens.

You'll find that at [JWTCreatorService](src/main/java/com/kapeta/spring/security/provider/JWTCreatorService.java)

### Consumer

The consumer will also be auto-configured using the configuration in
[KapetaSecurityConsumerConfig](src/main/java/com/kapeta/spring/security/consumer/KapetaSecurityConsumerConfig.java).

Read the class to see what can be overridden and how it works

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
