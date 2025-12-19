package com.tech_blog.prod.application.dto.responses.auth;

public record LoginResponse(
        String tokenType,
        String accessToken
) {
}
