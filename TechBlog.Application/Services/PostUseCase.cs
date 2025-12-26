using TechBlog.Application.DTOs.Posts;
using TechBlog.Application.Ports;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Ports;
using TechBlog.Domain.Services;

namespace TechBlog.Application.Services;

public class PostUseCase : IPostUseCase
{
    private readonly IPostDomainService _postDomainService;
    private readonly IPostRepository _postRepository;
    private readonly IUnitOfWork _unitOfWork;

    public PostUseCase(IPostDomainService postDomainService, IPostRepository postRepository, IUnitOfWork unitOfWork)
    {
        _postDomainService = postDomainService;
        _postRepository = postRepository;
        _unitOfWork = unitOfWork;
    }

    public async Task<PostResponse> CreateAsync(PostRequest request, Guid authorId, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        var post = new Post
        {
            Title = request.Title,
            Slug = request.Slug,
            Content = request.Content,
            CategoryId = request.CategoryId,
            AuthorId = authorId,
            Featured = request.Featured
        };

        var created = await _postDomainService.CreateAsync(post, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(created);
    }

    public async Task<PostResponse> UpdateAsync(PostRequest request, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        if (request.Id is null)
        {
            throw new ArgumentException("Id is required to update");
        }

        var existing = await _postRepository.GetByIdAsync(request.Id.Value, cancellationToken) ?? throw new KeyNotFoundException("Post not found");
        existing.Title = request.Title;
        existing.Slug = request.Slug;
        existing.Content = request.Content;
        existing.CategoryId = request.CategoryId;
        existing.Featured = request.Featured;

        var updated = await _postDomainService.UpdateAsync(existing, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(updated);
    }

    public async Task DeleteAsync(Guid id, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        await _postDomainService.DeleteAsync(id, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
    }

    public async Task<PostResponse?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var post = await _postRepository.GetByIdAsync(id, cancellationToken);
        return post is null ? null : Map(post);
    }

    public async Task<IReadOnlyCollection<PostResponse>> ListAsync(CancellationToken cancellationToken = default)
    {
        var posts = await _postRepository.ListAsync(cancellationToken);
        return posts.Select(Map).ToList();
    }

    private static PostResponse Map(Post post) => new(post.Id, post.Title, post.Slug, post.Content, post.CategoryId, post.AuthorId, post.Featured, post.CreatedAt);
}
