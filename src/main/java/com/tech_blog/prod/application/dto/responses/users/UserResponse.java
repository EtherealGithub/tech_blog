package com.tech_blog.prod.application.dto.responses.users;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        Boolean isUser,
        Boolean isAdmin,
        Boolean isSuperadmin,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
