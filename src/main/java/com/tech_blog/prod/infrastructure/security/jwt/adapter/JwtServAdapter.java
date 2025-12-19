package com.tech_blog.prod.infrastructure.security.jwt.adapter;

import com.tech_blog.prod.infrastructure.security.jwt.port.IJwtServPort;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//import static com.tech_blog.prod.infrastructure.security.jwt.JWTConstants.*;

@Service
public class JwtServAdapter implements IJwtServPort {

    // Configuration
    private final String secret = "1f3c7218ea76d594351145add5dfc462030bc0431edf94a278b8f70a671bf7c6";
    private final long expirationMiliseconds = 5000L * 60L; // 5 minutos

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Implementación
    @Override
    public String generateToken(Long userId, String username, Collection<? extends GrantedAuthority> authorities) {

        long now = System.currentTimeMillis();

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMiliseconds))
                .claim("uid", userId)
                .claim("roles", roles)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            return isTokenExpired(token) == false;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String usernameFromToken(String token) {
        return parseAllClaims(token).getSubject();
    }

    // Helpers
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return parseAllClaims(token).getExpiration();
    }


    private Long extractUserId(String token) {
        Object uid = parseAllClaims(token).get("uid");
        if (uid instanceof Integer i) return i.longValue();
        if (uid instanceof Long l) return l;
        if (uid instanceof String s) return Long.parseLong(s);
        throw new IllegalArgumentException("Invalid uid claim");
    }

    private List<String> extractRoles(String token) {
        Object roles = parseAllClaims(token).get("roles");
        if (roles instanceof List<?> list) {
            return (List<String>) list;
        }
        return List.of();
    }

    private Claims parseAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (UnsupportedJwtException | MalformedJwtException | io.jsonwebtoken.security.SignatureException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

}
