namespace TechBlog.Infrastructure.Entities;

public class CommentEntity
{
    public Guid Id { get; set; }
    public Guid PostId { get; set; }
    public Guid AuthorId { get; set; }
    public string Content { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    public PostEntity? Post { get; set; }
    public UserEntity? Author { get; set; }
}
