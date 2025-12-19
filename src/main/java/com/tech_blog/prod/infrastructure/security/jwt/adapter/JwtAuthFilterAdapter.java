package com.tech_blog.prod.infrastructure.security.jwt.adapter;

import com.tech_blog.prod.infrastructure.security.jwt.port.IJwtServPort;
import com.tech_blog.prod.infrastructure.security.user.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilterAdapter extends OncePerRequestFilter {

    private final IJwtServPort iJwtServPort;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilterAdapter(IJwtServPort iJwtServPort, CustomUserDetailsService userDetailsService) {
        this.iJwtServPort = iJwtServPort;
        this.userDetailsService = userDetailsService;
    }

    // Implementación
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);

            if (!iJwtServPort.isTokenValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            final String username = iJwtServPort.usernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authSpringSecurity =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authSpringSecurity.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authSpringSecurity);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e ) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
