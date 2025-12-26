namespace TechBlog.Application.DTOs.Posts;

public class PostRequest
{
    public Guid? Id { get; set; }
    public string Title { get; set; } = string.Empty;
    public string Slug { get; set; } = string.Empty;
    public string Content { get; set; } = string.Empty;
    public Guid CategoryId { get; set; }
    public bool Featured { get; set; }
}
