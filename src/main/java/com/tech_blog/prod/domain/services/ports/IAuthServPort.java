package com.tech_blog.prod.domain.services.ports;

import com.tech_blog.prod.application.dto.requests.auth.LoginRequest;
import com.tech_blog.prod.application.dto.responses.auth.LoginResponse;

public interface IAuthServPort {
    String getCurrentUserId();
}
