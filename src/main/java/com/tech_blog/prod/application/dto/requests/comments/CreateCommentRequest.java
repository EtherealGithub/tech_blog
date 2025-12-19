package com.tech_blog.prod.application.dto.requests.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
    @NotBlank @Size(max = 1000) String content,
    @NotNull Long postId,
    @NotNull Long userId
) {}
