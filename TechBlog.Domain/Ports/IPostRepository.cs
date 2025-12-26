using TechBlog.Domain.Entities;

namespace TechBlog.Domain.Ports;

public interface IPostRepository
{
    Task<Post?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default);
    Task<Post?> GetBySlugAsync(string slug, CancellationToken cancellationToken = default);
    Task<IReadOnlyCollection<Post>> ListAsync(CancellationToken cancellationToken = default);
    Task AddAsync(Post post, CancellationToken cancellationToken = default);
    Task UpdateAsync(Post post, CancellationToken cancellationToken = default);
    Task RemoveAsync(Post post, CancellationToken cancellationToken = default);
}
