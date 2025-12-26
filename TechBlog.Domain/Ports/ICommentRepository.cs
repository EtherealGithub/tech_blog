using TechBlog.Domain.Entities;

namespace TechBlog.Domain.Ports;

public interface ICommentRepository
{
    Task<Comment?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default);
    Task<IReadOnlyCollection<Comment>> ListByPostAsync(Guid postId, CancellationToken cancellationToken = default);
    Task AddAsync(Comment comment, CancellationToken cancellationToken = default);
    Task RemoveAsync(Comment comment, CancellationToken cancellationToken = default);
}
