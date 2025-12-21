package com.tech_blog.prod.infrastructure.security.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtDecoder {

    // Attributes
    private final JwtConfig jwtProperties;

    // Constructor
    public JwtDecoder(JwtConfig jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Methods
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtProperties.getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            return e.getClaims();

        } catch (UnsupportedJwtException | MalformedJwtException | io.jsonwebtoken.security.SignatureException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public Long extractUserId(String token) {
        Object uid = parseToken(token).get("uid");
        if (uid instanceof Integer i) return i.longValue();
        if (uid instanceof Long l) return l;
        if (uid instanceof String s) return Long.parseLong(s);
        throw new IllegalArgumentException("Invalid uid claim");
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object roles = parseToken(token).get("roles");
        if (roles instanceof List<?> list) {
            return (List<String>) list;
        }
        return List.of();
    }

}
