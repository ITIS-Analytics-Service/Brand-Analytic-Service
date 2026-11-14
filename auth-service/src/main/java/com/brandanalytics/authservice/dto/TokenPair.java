package com.brandanalytics.authservice.dto;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
