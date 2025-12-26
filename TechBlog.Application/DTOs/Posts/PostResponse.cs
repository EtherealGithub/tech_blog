namespace TechBlog.Application.DTOs.Posts;

public record PostResponse(Guid Id, string Title, string Slug, string Content, Guid CategoryId, Guid AuthorId, bool Featured, DateTime CreatedAt);
