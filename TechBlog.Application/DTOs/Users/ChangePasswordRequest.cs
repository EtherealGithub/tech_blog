namespace TechBlog.Application.DTOs.Users;

public class ChangePasswordRequest
{
    public Guid Id { get; set; }
    public string NewPassword { get; set; } = string.Empty;
}
