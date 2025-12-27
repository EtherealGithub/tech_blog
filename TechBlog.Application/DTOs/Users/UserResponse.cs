namespace TechBlog.Application.DTOs.Users;

public record UserResponse(Guid Id, string Username, string Email, bool IsSuperAdmin, bool IsAdmin, bool IsUser, DateTime CreatedAt);
