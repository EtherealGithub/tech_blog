package com.tech_blog.prod.infrastructure.controllers;

import com.tech_blog.prod.application.dto.requests.users.ChangePasswordRequest;
import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRoleRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.application.usecases.ports.IUserUsePort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserUsePort iUserUsePort;

    public UserController(IUserUsePort iUserUsePort) {
        this.iUserUsePort = iUserUsePort;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(iUserUsePort.getCurrentUser());
    }

    @GetMapping("/list_users")
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(iUserUsePort.listUsers());
    }

    @GetMapping("/read_user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id ) {
        return ResponseEntity.ok(iUserUsePort.getUserById(id));
    }

    @PostMapping("/create_user")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserResponse created = iUserUsePort.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update_user/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        UserResponse updated = iUserUsePort.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/change_password/{id}")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        iUserUsePort.changePasswordById(id, changePasswordRequest.newPassword());
        return ResponseEntity.noContent().header("X-Message", "Password updated successfully").build();
    }

    @PatchMapping("/update_user_role/{id}")
    public ResponseEntity<UserResponse> updateUserRoleById(@PathVariable Long id, @RequestBody @Valid UpdateUserRoleRequest updateUserRoleRequest) {
        UserResponse updated = iUserUsePort.updateUserRoleById(id, updateUserRoleRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        iUserUsePort.deleteUserById(id);
        return ResponseEntity.noContent().header("X-Message", "User deleted successfully").build();
    }

}
