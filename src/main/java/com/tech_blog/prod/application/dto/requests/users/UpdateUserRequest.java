package com.tech_blog.prod.application.dto.requests.users;

import jakarta.validation.constraints.*;

public record UpdateUserRequest(
        @NotNull @NotEmpty @NotBlank @Size(max = 50) String username,
        @NotNull @NotEmpty @NotBlank @Email @Size(max = 120) String email
) {
}
