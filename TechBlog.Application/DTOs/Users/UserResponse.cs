using TechBlog.Domain.Enums;

namespace TechBlog.Application.DTOs.Users;

public record UserResponse(Guid Id, string Username, string Email, RoleType Role, DateTime CreatedAt);
