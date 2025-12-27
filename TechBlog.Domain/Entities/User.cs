using TechBlog.Domain.Enums;

namespace TechBlog.Domain.Entities;

public class User
{
    public Guid Id { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string PasswordHash { get; set; } = string.Empty;
    public bool IsSuperAdmin { get; set; }
    public bool IsAdmin { get; set; }
    public bool IsUser { get; set; }
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    public RoleType GetHighestRole()
    {
        if (IsSuperAdmin) return RoleType.SuperAdmin;
        if (IsAdmin) return RoleType.Admin;
        return RoleType.User;
    }
}
