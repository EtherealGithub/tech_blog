using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Exceptions;
using TechBlog.Domain.Ports;

namespace TechBlog.Domain.Services;

public interface IPostDomainService
{
    Task<Post> CreateAsync(Post post, RoleType performedBy, CancellationToken cancellationToken = default);
    Task<Post> UpdateAsync(Post post, RoleType performedBy, CancellationToken cancellationToken = default);
    Task DeleteAsync(Guid id, RoleType performedBy, CancellationToken cancellationToken = default);
}

public class PostDomainService : IPostDomainService
{
    private readonly IPostRepository _postRepository;
    private readonly ICategoryRepository _categoryRepository;
    private readonly IUserRepository _userRepository;

    public PostDomainService(IPostRepository postRepository, ICategoryRepository categoryRepository, IUserRepository userRepository)
    {
        _postRepository = postRepository;
        _categoryRepository = categoryRepository;
        _userRepository = userRepository;
    }

    public async Task<Post> CreateAsync(Post post, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        await EnsureAuthorization(performedBy);
        await EnsureReferences(post, cancellationToken);

        post.Id = Guid.NewGuid();
        post.Title = post.Title.Trim();
        post.Name = post.Name.Trim();
        post.Content = post.Content.Trim();
        post.CreatedAt = DateTime.UtcNow;

        await EnsureNameUnique(post, cancellationToken);
        await _postRepository.AddAsync(post, cancellationToken);
        return post;
    }

    public async Task<Post> UpdateAsync(Post post, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        await EnsureAuthorization(performedBy);
        await EnsureReferences(post, cancellationToken);

        post.Title = post.Title.Trim();
        post.Name = post.Name.Trim();
        post.Content = post.Content.Trim();

        await EnsureNameUnique(post, cancellationToken, post.Id);
        await _postRepository.UpdateAsync(post, cancellationToken);
        return post;
    }

    public async Task DeleteAsync(Guid id, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        await EnsureAuthorization(performedBy);
        var existing = await _postRepository.GetByIdAsync(id, cancellationToken) ?? throw new NotFoundException("Post not found");
        await _postRepository.RemoveAsync(existing, cancellationToken);
    }

    private async Task EnsureNameUnique(Post post, CancellationToken cancellationToken, Guid? currentId = null)
    {
        var existing = await _postRepository.GetByNameAsync(post.Name, cancellationToken);
        if (existing is not null && existing.Id != currentId)
        {
            throw new ValidationException("Post name already exists");
        }
    }

    private async Task EnsureReferences(Post post, CancellationToken cancellationToken)
    {
        if (await _categoryRepository.GetByIdAsync(post.CategoryId, cancellationToken) is null)
        {
            throw new ValidationException("Category does not exist");
        }

        if (await _userRepository.GetByIdAsync(post.AuthorId, cancellationToken) is null)
        {
            throw new ValidationException("Author does not exist");
        }
    }

    private static Task EnsureAuthorization(RoleType role)
    {
        if (role is RoleType.Admin or RoleType.SuperAdmin)
        {
            return Task.CompletedTask;
        }

        throw new AuthorizationException("Role cannot manage posts");
    }
}
