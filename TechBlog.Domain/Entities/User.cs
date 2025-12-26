using TechBlog.Domain.Enums;

namespace TechBlog.Domain.Entities;

public class User
{
    public Guid Id { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string PasswordHash { get; set; } = string.Empty;
    public RoleType Role { get; set; } = RoleType.User;
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}
