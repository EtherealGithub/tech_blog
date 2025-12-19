package com.tech_blog.prod.application.dto.requests.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
    @NotBlank @Size(max = 150) String title,
    @NotBlank String content,
    @NotNull Long userId,
    @NotNull Long categoryId,
    Boolean isFeatured
) {}
