namespace TechBlog.Application.DTOs.Comments;

public class CommentRequest
{
    public Guid PostId { get; set; }
    public string Content { get; set; } = string.Empty;
}
