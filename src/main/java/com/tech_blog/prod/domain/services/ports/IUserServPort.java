package com.tech_blog.prod.domain.services.ports;

import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.infrastructure.database.entities.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserServPort {

    UserEntity getUserEntityById(Long id);

    UserResponse getCurrentUser();
    List<UserResponse> listUsers(Authentication authentication);
    UserResponse getUserById(Long id, Authentication authentication);
    UserResponse createUser(CreateUserRequest createUserRequest, Authentication authentication);
    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest, Authentication authentication);
    void changePasswordById(Long id, String newPassword, Authentication authentication);
    void deleteUserById(Long id, Authentication authentication);
}
