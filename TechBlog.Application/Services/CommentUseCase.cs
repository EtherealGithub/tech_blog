using TechBlog.Application.DTOs.Comments;
using TechBlog.Application.Ports;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Ports;
using TechBlog.Domain.Services;

namespace TechBlog.Application.Services;

public class CommentUseCase : ICommentUseCase
{
    private readonly ICommentDomainService _commentDomainService;
    private readonly ICommentRepository _commentRepository;
    private readonly IUnitOfWork _unitOfWork;

    public CommentUseCase(ICommentDomainService commentDomainService, ICommentRepository commentRepository, IUnitOfWork unitOfWork)
    {
        _commentDomainService = commentDomainService;
        _commentRepository = commentRepository;
        _unitOfWork = unitOfWork;
    }

    public async Task<CommentResponse> CreateAsync(CommentRequest request, Guid authorId, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        var comment = new Comment
        {
            PostId = request.PostId,
            AuthorId = authorId,
            Content = request.Content
        };

        var created = await _commentDomainService.CreateAsync(comment, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(created);
    }

    public async Task DeleteAsync(Guid id, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        await _commentDomainService.DeleteAsync(id, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
    }

    public async Task<IReadOnlyCollection<CommentResponse>> ListByPostAsync(Guid postId, CancellationToken cancellationToken = default)
    {
        var comments = await _commentRepository.ListByPostAsync(postId, cancellationToken);
        return comments.Select(Map).ToList();
    }

    public async Task<CommentResponse?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var comment = await _commentRepository.GetByIdAsync(id, cancellationToken);
        return comment is null ? null : Map(comment);
    }

    private static CommentResponse Map(Comment comment) => new(comment.Id, comment.PostId, comment.AuthorId, comment.Content, comment.CreatedAt);
}
