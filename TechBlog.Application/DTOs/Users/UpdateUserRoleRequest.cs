using TechBlog.Domain.Enums;

namespace TechBlog.Application.DTOs.Users;

public class UpdateUserRoleRequest
{
    public Guid Id { get; set; }
    public RoleType Role { get; set; } = RoleType.User;
}
