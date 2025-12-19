package com.tech_blog.prod.application.dto.requests.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull @NotEmpty @NotBlank String username,
        @NotNull @NotEmpty @NotBlank String password
) {
}
