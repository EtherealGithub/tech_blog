package com.tech_blog.prod.application.usecases.ports;

import com.tech_blog.prod.application.dto.requests.auth.LoginRequest;
import com.tech_blog.prod.application.dto.responses.auth.LoginResponse;

public interface IAuthUsePort {
    LoginResponse login(LoginRequest loginRequest);
}
