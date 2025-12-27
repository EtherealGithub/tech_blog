namespace TechBlog.Infrastructure.Entities;

public class CategoryEntity
{
    public Guid Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;

    public ICollection<PostEntity> Posts { get; set; } = new List<PostEntity>();
}
