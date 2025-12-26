namespace TechBlog.Infrastructure.Entities;

public class PostEntity
{
    public Guid Id { get; set; }
    public string Title { get; set; } = string.Empty;
    public string Slug { get; set; } = string.Empty;
    public string Content { get; set; } = string.Empty;
    public Guid CategoryId { get; set; }
    public Guid AuthorId { get; set; }
    public bool Featured { get; set; }
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    public CategoryEntity? Category { get; set; }
    public UserEntity? Author { get; set; }
    public ICollection<CommentEntity> Comments { get; set; } = new List<CommentEntity>();
}
