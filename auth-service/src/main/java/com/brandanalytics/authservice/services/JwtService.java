package com.brandanalytics.authservice.services;

import com.brandanalytics.authservice.dto.TokenPair;
import com.brandanalytics.authservice.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    private final JwtProperties jwtProperties;

    private final JwtParser parser;

    private static final String HEADER_TYPE = "JWT";

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.parser = Jwts.parser()
                .verifyWith(getSignInKey())
                .build();
    }

    public TokenPair generateTokenPair(Authentication authentication) {
        return new TokenPair(
                generateAccessToken(authentication),
                generateRefreshToken(authentication)
        );
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, jwtProperties.accessExpiration(), new HashMap<>());
    }

    public String generateRefreshToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        return generateToken(authentication, jwtProperties.refreshExpiration(), claims);
    }


    private String generateToken(Authentication authentication, Duration expiration, Map<String, Object> claims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Instant now = Instant.now();
        Instant expiryDate = now.plus(expiration);

        return Jwts.builder()
                .header()
                .add("typ", HEADER_TYPE)
                .and()
                .subject(userPrincipal.getUsername())
                .issuedAt(Date.from(now))
                .claims(claims)
                .expiration(Date.from(expiryDate))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        String username = extractUsernameFromToken(token);

        return username != null && username.equals(userDetails.getUsername());
    }

    public boolean isValidToken(String token) {
        return extractAllClaims(token) != null;
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);

        if (claims == null) {
            return false;
        }

        return "refresh".equals(claims.get("tokenType"));
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);

        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    private Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = parser.parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return claims;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
