namespace TechBlog.Application.DTOs.Users;

public class UserCreateRequest
{
    public string Username { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string Password { get; set; } = string.Empty;
    public RoleType Role { get; set; } = RoleType.User;
    public bool IsSuperAdmin { get; set; }
    public bool IsAdmin { get; set; }
    public bool IsUser { get; set; }
}
