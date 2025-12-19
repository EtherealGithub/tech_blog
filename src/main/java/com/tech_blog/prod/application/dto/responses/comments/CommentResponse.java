package com.tech_blog.prod.application.dto.responses.comments;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long postId,
        Long userId,
        String username
) {}
