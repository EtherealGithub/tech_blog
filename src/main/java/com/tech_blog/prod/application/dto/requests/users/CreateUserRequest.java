package com.tech_blog.prod.application.dto.requests.users;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotNull @NotEmpty @NotBlank @Size(max = 50) String username,
        @NotNull @NotEmpty @NotBlank @Email @Size(max = 120) String email,
        @NotNull @NotEmpty @NotBlank @Size(min = 4, max = 100) String password,

        @NotNull UserRole role
) {
    public enum UserRole { USER, ADMIN, SUPERADMIN }
}
