package com.tech_blog.prod.application.dto.requests.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
    @NotBlank @Size(min = 6, max = 100) String newPassword
) {}
