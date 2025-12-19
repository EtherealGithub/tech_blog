package com.tech_blog.prod.application.usecases.ports;

import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserUsePort {
    UserResponse getCurrentUser(Authentication authentication);
    List<UserResponse> listUsers(Authentication authentication);
    UserResponse getUserById(Long id, Authentication authentication);
    UserResponse createUser(CreateUserRequest createUserRequest, Authentication authentication);
    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest, Authentication authentication);
    void changePasswordById(Long id, String newPassword, Authentication authentication);
    void deleteUserById(Long id, Authentication authentication);
}
