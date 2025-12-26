using TechBlog.Application.DTOs.Comments;
using TechBlog.Domain.Enums;

namespace TechBlog.Application.Ports;

public interface ICommentUseCase
{
    Task<CommentResponse> CreateAsync(CommentRequest request, Guid authorId, RoleType performerRole, CancellationToken cancellationToken = default);
    Task DeleteAsync(Guid id, RoleType performerRole, CancellationToken cancellationToken = default);
    Task<IReadOnlyCollection<CommentResponse>> ListByPostAsync(Guid postId, CancellationToken cancellationToken = default);
}
