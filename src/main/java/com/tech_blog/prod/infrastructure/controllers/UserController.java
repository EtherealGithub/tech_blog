package com.tech_blog.prod.infrastructure.controllers;

import com.tech_blog.prod.application.dto.requests.users.ChangePasswordRequest;
import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.application.usecases.ports.IUserUsePort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        return ResponseEntity.ok(iUserUsePort.getCurrentUser(authentication));
    }

    @GetMapping("/list_users")
    public ResponseEntity<List<UserResponse>> listUsers(Authentication authentication) {
        return ResponseEntity.ok(iUserUsePort.listUsers(authentication));
    }

    @GetMapping("/read_user/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(iUserUsePort.getUserById(id, authentication));
    }

    @PostMapping("/create_user")
    public ResponseEntity<UserResponse> create(
            @RequestBody @Valid CreateUserRequest createUserRequest,
            Authentication authentication
    ) {
        UserResponse created = iUserUsePort.createUser(createUserRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update_user/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest updateUserRequest,
            Authentication authentication
    ) {
        return ResponseEntity.ok(iUserUsePort.updateUser(id, updateUserRequest, authentication));
    }

    @PatchMapping("/change_password/{id}/")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            Authentication authentication
    ) {
        iUserUsePort.changePasswordById(id, changePasswordRequest.newPassword(), authentication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        iUserUsePort.deleteUserById(id, authentication);
        return ResponseEntity.noContent().build();
    }


}
