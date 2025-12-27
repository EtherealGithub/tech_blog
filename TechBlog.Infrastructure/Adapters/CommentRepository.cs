using Microsoft.EntityFrameworkCore;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Ports;
using TechBlog.Infrastructure.Entities;
using TechBlog.Infrastructure.Persistence;

namespace TechBlog.Infrastructure.Adapters;

public class CommentRepository : ICommentRepository
{
    private readonly TechBlogDbContext _dbContext;

    public CommentRepository(TechBlogDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task AddAsync(Comment comment, CancellationToken cancellationToken = default)
    {
        await _dbContext.Comments.AddAsync(ToEntity(comment), cancellationToken);
    }

    public async Task RemoveAsync(Comment comment, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Comments.FirstOrDefaultAsync(c => c.Id == comment.Id, cancellationToken);
        if (entity is not null)
        {
            _dbContext.Comments.Remove(entity);
        }
    }

    public async Task<Comment?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Comments.AsNoTracking().FirstOrDefaultAsync(c => c.Id == id, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<IReadOnlyCollection<Comment>> ListByPostAsync(Guid postId, CancellationToken cancellationToken = default)
    {
        var comments = await _dbContext.Comments.AsNoTracking().Where(c => c.PostId == postId).ToListAsync(cancellationToken);
        return comments.Select(ToDomain).ToList();
    }

    private static Comment ToDomain(CommentEntity entity) => new()
    {
        Id = entity.Id,
        PostId = entity.PostId,
        AuthorId = entity.AuthorId,
        Content = entity.Content,
        CreatedAt = entity.CreatedAt
    };

    private static CommentEntity ToEntity(Comment comment) => new()
    {
        Id = comment.Id,
        PostId = comment.PostId,
        AuthorId = comment.AuthorId,
        Content = comment.Content,
        CreatedAt = comment.CreatedAt
    };
}
