package com.tech_blog.prod.infrastructure.security.jwt.port;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface IJwtServPort {
    String generateToken(Long userId, String username, Collection<? extends GrantedAuthority> authorities);
    boolean isTokenValid(String token);
    String usernameFromToken(String token);
}
