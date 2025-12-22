package com.tech_blog.prod.infrastructure.controllers;


import com.tech_blog.prod.application.dto.requests.auth.RegisterRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.application.usecases.ports.IUserUsePort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final IUserUsePort iUserUsePort;

    public PublicController(IUserUsePort iUserUsePort) {
        this.iUserUsePort = iUserUsePort;
    }

    @PostMapping("/own_register_user")
    public ResponseEntity<UserResponse> ownRegisterUser(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(iUserUsePort.ownRegister(registerRequest));
    }
}
