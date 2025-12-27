namespace TechBlog.Application.DTOs.Users;

public class UserUpdateRequest
{
    public Guid Id { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
}
