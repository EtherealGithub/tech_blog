package com.tech_blog.prod.infrastructure.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtService {

    // Inyected dependencies
    private final JwtConfig jwtConfig;
    private final JwtDecoder jwtDecoder;

    // Constructor
    public JwtService(JwtConfig jwtConfig, JwtDecoder jwtDecoder) {
        this.jwtConfig = jwtConfig;
        this.jwtDecoder = jwtDecoder;
    }

    // Methods
    public String generateToken(String username, List<String> roles, long longCurrentTime) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(longCurrentTime))
                .expiration(new Date(longCurrentTime + jwtConfig.getExpirationMs()))
                .claim("roles", roles)
                .claim("type", "access")
                .signWith(jwtConfig.getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            return !jwtDecoder.isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String usernameFromToken(String token) {
        return jwtDecoder.extractUsername(token);
    }

}
