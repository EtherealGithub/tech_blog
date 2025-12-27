using TechBlog.Application.DTOs.Posts;
using TechBlog.Domain.Enums;

namespace TechBlog.Application.Ports;

public interface IPostUseCase
{
    Task<PostResponse> CreateAsync(PostRequest request, Guid authorId, RoleType performerRole, CancellationToken cancellationToken = default);
    Task<PostResponse> UpdateAsync(PostRequest request, RoleType performerRole, CancellationToken cancellationToken = default);
    Task DeleteAsync(Guid id, RoleType performerRole, CancellationToken cancellationToken = default);
    Task<PostResponse?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default);
    Task<IReadOnlyCollection<PostResponse>> ListAsync(CancellationToken cancellationToken = default);
}
