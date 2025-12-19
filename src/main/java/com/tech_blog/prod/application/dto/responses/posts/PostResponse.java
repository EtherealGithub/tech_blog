package com.tech_blog.prod.application.dto.responses.posts;

import java.time.LocalDateTime;

public record PostResponse(
    Long id,
    String title,
    String content,
    Boolean isFeatured,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long userId,
    String username,
    Long categoryId,
    String categoryName
) {}
