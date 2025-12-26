namespace TechBlog.Domain.Entities;

public class Post
{
    public Guid Id { get; set; }
    public string Title { get; set; } = string.Empty;
    public string Slug { get; set; } = string.Empty;
    public string Content { get; set; } = string.Empty;
    public Guid CategoryId { get; set; }
    public Guid AuthorId { get; set; }
    public bool Featured { get; set; }
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}
