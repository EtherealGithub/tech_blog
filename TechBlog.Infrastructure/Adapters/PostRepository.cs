using Microsoft.EntityFrameworkCore;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Ports;
using TechBlog.Infrastructure.Entities;
using TechBlog.Infrastructure.Persistence;

namespace TechBlog.Infrastructure.Adapters;

public class PostRepository : IPostRepository
{
    private readonly TechBlogDbContext _dbContext;

    public PostRepository(TechBlogDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task AddAsync(Post post, CancellationToken cancellationToken = default)
    {
        var entity = ToEntity(post);
        await _dbContext.Posts.AddAsync(entity, cancellationToken);
    }

    public async Task UpdateAsync(Post post, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Posts.FirstOrDefaultAsync(x => x.Id == post.Id, cancellationToken);
        if (entity is null)
        {
            throw new KeyNotFoundException("Post not found");
        }

        entity.Title = post.Title;
        entity.Slug = post.Slug;
        entity.Content = post.Content;
        entity.CategoryId = post.CategoryId;
        entity.Featured = post.Featured;
        _dbContext.Posts.Update(entity);
    }

    public async Task RemoveAsync(Post post, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Posts.FirstOrDefaultAsync(x => x.Id == post.Id, cancellationToken);
        if (entity is not null)
        {
            _dbContext.Posts.Remove(entity);
        }
    }

    public async Task<Post?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Posts.AsNoTracking().FirstOrDefaultAsync(p => p.Id == id, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<Post?> GetBySlugAsync(string slug, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Posts.AsNoTracking().FirstOrDefaultAsync(p => p.Slug == slug, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<IReadOnlyCollection<Post>> ListAsync(CancellationToken cancellationToken = default)
    {
        var posts = await _dbContext.Posts.AsNoTracking().ToListAsync(cancellationToken);
        return posts.Select(ToDomain).ToList();
    }

    private static Post ToDomain(PostEntity entity) => new()
    {
        Id = entity.Id,
        Title = entity.Title,
        Slug = entity.Slug,
        Content = entity.Content,
        CategoryId = entity.CategoryId,
        AuthorId = entity.AuthorId,
        Featured = entity.Featured,
        CreatedAt = entity.CreatedAt
    };

    private static PostEntity ToEntity(Post post) => new()
    {
        Id = post.Id,
        Title = post.Title,
        Slug = post.Slug,
        Content = post.Content,
        CategoryId = post.CategoryId,
        AuthorId = post.AuthorId,
        Featured = post.Featured,
        CreatedAt = post.CreatedAt
    };
}
