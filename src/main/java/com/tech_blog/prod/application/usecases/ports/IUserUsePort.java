package com.tech_blog.prod.application.usecases.ports;

import com.tech_blog.prod.application.dto.requests.auth.RegisterRequest;
import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRoleRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserUsePort {
    UserResponse getCurrentUser();
    List<UserResponse> listUsers();
    UserResponse getUserById(Long id);
    UserResponse createUser(CreateUserRequest createUserRequest);
    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);
    void changePasswordById(Long id, String newPassword);
    UserResponse updateUserRoleById(Long id, UpdateUserRoleRequest updateUserRoleRequest);
    void deleteUserById(Long id);

    UserResponse ownRegister(RegisterRequest registerRequest);
}
