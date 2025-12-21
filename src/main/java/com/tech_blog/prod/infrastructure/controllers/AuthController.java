package com.tech_blog.prod.infrastructure.controllers;

import com.tech_blog.prod.application.dto.requests.auth.LoginRequest;
import com.tech_blog.prod.application.dto.responses.auth.LoginResponse;
import com.tech_blog.prod.infrastructure.security.user.AuthUserDetails;
import com.tech_blog.prod.infrastructure.security.util.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Inyeccion de dependencias
    private final JwtService jwtService;
    private final AuthenticationManager iAuthenticationManager;

    // Constructor
    public AuthController(JwtService jwtService, AuthenticationManager iAuthenticationManager) {
        this.jwtService = jwtService;
        this.iAuthenticationManager = iAuthenticationManager;
    }

    // Endpoints
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        Authentication auth = iAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        AuthUserDetails principal = Optional.ofNullable((AuthUserDetails) auth.getPrincipal())
                .orElseThrow(() -> new IllegalStateException("Authentication principal is null"));

        String username = principal.getUsername();

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        long longCurrentTime = System.currentTimeMillis();

        String accessToken = jwtService.generateToken(username, roles, longCurrentTime);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse("Bearer", accessToken));
    }





}
