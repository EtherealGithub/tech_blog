package com.tech_blog.prod.domain.services.ports;

import com.tech_blog.prod.application.constants.Role;
import com.tech_blog.prod.application.dto.requests.auth.LoginRequest;
import com.tech_blog.prod.application.dto.responses.auth.LoginResponse;
import com.tech_blog.prod.infrastructure.database.entities.UserEntity;

import java.util.Set;

public interface IAuthServPort {
    String getCurrentUserId();
    UserEntity getCurrentUserEntity();
    Role getCurrentUserStrongestRole();
    Set<Role> getCurrentUserRoles();
}
