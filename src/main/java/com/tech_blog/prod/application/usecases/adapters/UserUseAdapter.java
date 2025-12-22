package com.tech_blog.prod.application.usecases.adapters;

import com.tech_blog.prod.application.dto.requests.auth.RegisterRequest;
import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRoleRequest;
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
    public UserResponse getCurrentUser() {
        return iUSerServPort.getCurrentUser();
    }

    @Override
    public List<UserResponse> listUsers() {
        return iUSerServPort.listUsers();
    }

    @Override
    public UserResponse getUserById(Long id) {
        return iUSerServPort.getUserById(id);
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        return iUSerServPort.createUser(createUserRequest);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {
        return iUSerServPort.updateUser(id, updateUserRequest);
    }

    @Override
    public void changePasswordById(Long id, String newPassword) {
        iUSerServPort.changePasswordById(id, newPassword);
    }

    @Override
    public UserResponse updateUserRoleById(Long id, UpdateUserRoleRequest updateUserRoleRequest) {
        return iUSerServPort.updateUserRoleById(id, updateUserRoleRequest);
    }

    @Override
    public void deleteUserById(Long id) {
        iUSerServPort.deleteUserById(id);
    }

    @Override
    public UserResponse ownRegister(RegisterRequest registerRequest) {
        return iUSerServPort.ownRegister(registerRequest);
    }


}
