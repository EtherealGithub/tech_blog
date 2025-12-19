package com.tech_blog.prod.domain.services.adapters;

import com.tech_blog.prod.application.dto.requests.auth.LoginRequest;
import com.tech_blog.prod.application.dto.responses.auth.LoginResponse;
import com.tech_blog.prod.domain.services.ports.IAuthServPort;
import com.tech_blog.prod.infrastructure.security.jwt.port.IJwtServPort;
import com.tech_blog.prod.infrastructure.security.user.AuthUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServAdapter implements IAuthServPort {
    // Inyeccion de dependencias
    private final AuthenticationManager iAuthenticationManager;
    private final IJwtServPort iJwtServPort;

    // Constructor
    public AuthServAdapter(AuthenticationManager iAuthenticationManager, IJwtServPort iJwtServPort) {
        this.iAuthenticationManager = iAuthenticationManager;
        this.iJwtServPort = iJwtServPort;
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication auth = iAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        AuthUserDetails principal = (AuthUserDetails) auth.getPrincipal();

        String token = iJwtServPort.generateToken(
                principal.getUserId(),
                principal.getUsername(),
                principal.getAuthorities()
        );

        return new LoginResponse("Bearer", token);
    }
}
