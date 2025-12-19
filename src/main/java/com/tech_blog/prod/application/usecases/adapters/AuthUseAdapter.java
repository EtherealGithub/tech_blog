package com.tech_blog.prod.application.usecases.adapters;

import com.tech_blog.prod.application.dto.requests.auth.LoginRequest;
import com.tech_blog.prod.application.dto.responses.auth.LoginResponse;
import com.tech_blog.prod.application.usecases.ports.IAuthUsePort;
import com.tech_blog.prod.domain.services.ports.IAuthServPort;
import org.springframework.stereotype.Service;

@Service
public class AuthUseAdapter implements IAuthUsePort {

    // Inyección de dependencia
    private final IAuthServPort iAuthServPort;

    // Constructor
    public AuthUseAdapter(IAuthServPort iAuthServPort) {
        this.iAuthServPort = iAuthServPort;
    }

    // Implementación
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return iAuthServPort.login(loginRequest);
    }
}
