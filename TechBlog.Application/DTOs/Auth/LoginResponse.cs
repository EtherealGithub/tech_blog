using TechBlog.Domain.Enums;

namespace TechBlog.Application.DTOs.Auth;

public record LoginResponse(string Token, DateTime ExpiresAt, string Username, RoleType Role);
