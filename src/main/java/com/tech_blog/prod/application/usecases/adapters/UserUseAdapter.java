package com.tech_blog.prod.application.usecases.adapters;

import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.application.usecases.ports.IUserUsePort;
import com.tech_blog.prod.domain.services.ports.IUserServPort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserUseAdapter implements IUserUsePort {

    private final IUserServPort iUSerServPort;

    public UserUseAdapter(IUserServPort iUSerServPort) {
        this.iUSerServPort = iUSerServPort;
    }

    @Override
    public UserResponse getCurrentUser(Authentication authentication) {
        return iUSerServPort.getCurrentUser(authentication);
    }

    @Override
    public List<UserResponse> listUsers(Authentication authentication) {
        return iUSerServPort.listUsers(authentication);
    }

    @Override
    public UserResponse getUserById(Long id, Authentication authentication) {
        return iUSerServPort.getUserById(id, authentication);
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest, Authentication authentication) {
        return iUSerServPort.createUser(createUserRequest, authentication);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest, Authentication authentication) {
        return iUSerServPort.updateUser(id, updateUserRequest, authentication);
    }

    @Override
    public void changePasswordById(Long id, String newPassword, Authentication authentication) {
        iUSerServPort.changePasswordById(id, newPassword, authentication);
    }

    @Override
    public void deleteUserById(Long id, Authentication authentication) {
        iUSerServPort.deleteUserById(id, authentication);
    }
}
