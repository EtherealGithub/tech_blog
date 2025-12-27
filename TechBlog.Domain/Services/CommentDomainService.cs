using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Exceptions;
using TechBlog.Domain.Ports;

namespace TechBlog.Domain.Services;

public interface ICommentDomainService
{
    Task<Comment> CreateAsync(Comment comment, RoleType performedBy, CancellationToken cancellationToken = default);
    Task DeleteAsync(Guid id, RoleType performedBy, CancellationToken cancellationToken = default);
}

public class CommentDomainService : ICommentDomainService
{
    private readonly ICommentRepository _commentRepository;
    private readonly IPostRepository _postRepository;
    private readonly IUserRepository _userRepository;

    public CommentDomainService(ICommentRepository commentRepository, IPostRepository postRepository, IUserRepository userRepository)
    {
        _commentRepository = commentRepository;
        _postRepository = postRepository;
        _userRepository = userRepository;
    }

    public async Task<Comment> CreateAsync(Comment comment, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        await EnsureUserExists(comment.AuthorId, cancellationToken);
        await EnsurePostExists(comment.PostId, cancellationToken);

        comment.Id = Guid.NewGuid();
        comment.Content = comment.Content.Trim();
        comment.CreatedAt = DateTime.UtcNow;

        await _commentRepository.AddAsync(comment, cancellationToken);
        return comment;
    }

    public async Task DeleteAsync(Guid id, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        var comment = await _commentRepository.GetByIdAsync(id, cancellationToken) ?? throw new NotFoundException("Comment not found");
        if (performedBy is not RoleType.Admin and not RoleType.SuperAdmin && performedBy != RoleType.User)
        {
            throw new AuthorizationException("Invalid role for comment deletion");
        }

        await _commentRepository.RemoveAsync(comment, cancellationToken);
    }

    private async Task EnsureUserExists(Guid userId, CancellationToken cancellationToken)
    {
        if (await _userRepository.GetByIdAsync(userId, cancellationToken) is null)
        {
            throw new ValidationException("User does not exist");
        }
    }

    private async Task EnsurePostExists(Guid postId, CancellationToken cancellationToken)
    {
        if (await _postRepository.GetByIdAsync(postId, cancellationToken) is null)
        {
            throw new ValidationException("Post does not exist");
        }
    }
}
