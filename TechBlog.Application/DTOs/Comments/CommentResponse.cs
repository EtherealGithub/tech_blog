namespace TechBlog.Application.DTOs.Comments;

public record CommentResponse(Guid Id, Guid PostId, Guid AuthorId, string Content, DateTime CreatedAt);
