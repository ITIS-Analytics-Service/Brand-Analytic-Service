package com.brandanalytics.authservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        Duration accessExpiration,
        Duration refreshExpiration,
        String secret
) {}
