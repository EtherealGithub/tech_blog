using TechBlog.Domain.Enums;

namespace TechBlog.Infrastructure.Entities;

public class UserEntity
{
    public Guid Id { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string PasswordHash { get; set; } = string.Empty;
    public RoleType Role { get; set; } = RoleType.User;
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    public ICollection<PostEntity> Posts { get; set; } = new List<PostEntity>();
    public ICollection<CommentEntity> Comments { get; set; } = new List<CommentEntity>();
}
