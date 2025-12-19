package com.tech_blog.prod.infrastructure.controllers;

import com.tech_blog.prod.application.dto.requests.auth.LoginRequest;
import com.tech_blog.prod.application.dto.responses.auth.LoginResponse;
import com.tech_blog.prod.application.usecases.ports.IAuthUsePort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Inyeccion de dependencias
    private final IAuthUsePort iAuthUsePort;

    // Constructor
    public AuthController(IAuthUsePort iAuthUsePort) {
        this.iAuthUsePort = iAuthUsePort;
    }

    // Endpoints
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(iAuthUsePort.login(loginRequest));
    }


}
