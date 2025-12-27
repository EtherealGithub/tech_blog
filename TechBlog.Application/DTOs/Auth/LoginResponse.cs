namespace TechBlog.Application.DTOs.Auth;

public record LoginResponse(string Token, DateTime ExpiresAt, string Username, bool IsSuperAdmin, bool IsAdmin, bool IsUser);
