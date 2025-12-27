using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Exceptions;
using TechBlog.Domain.Ports;

namespace TechBlog.Domain.Services;

public interface ICategoryDomainService
{
    Task<Category> CreateAsync(Category category, RoleType performedBy, CancellationToken cancellationToken = default);
    Task<Category> UpdateAsync(Category category, RoleType performedBy, CancellationToken cancellationToken = default);
    Task DeleteAsync(Guid id, RoleType performedBy, CancellationToken cancellationToken = default);
}

public class CategoryDomainService : ICategoryDomainService
{
    private readonly ICategoryRepository _categoryRepository;

    public CategoryDomainService(ICategoryRepository categoryRepository)
    {
        _categoryRepository = categoryRepository;
    }

    public async Task<Category> CreateAsync(Category category, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        EnsureAdmin(performedBy);
        category.Id = Guid.NewGuid();
        category.Name = category.Name.Trim();
        category.Description = category.Description.Trim();
        await EnsureUniqueAsync(category, cancellationToken);
        await _categoryRepository.AddAsync(category, cancellationToken);
        return category;
    }

    public async Task<Category> UpdateAsync(Category category, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        EnsureAdmin(performedBy);
        category.Name = category.Name.Trim();
        category.Description = category.Description.Trim();
        await EnsureUniqueAsync(category, cancellationToken, category.Id);
        await _categoryRepository.UpdateAsync(category, cancellationToken);
        return category;
    }

    public async Task DeleteAsync(Guid id, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        EnsureAdmin(performedBy);
        var existing = await _categoryRepository.GetByIdAsync(id, cancellationToken) ?? throw new NotFoundException("Category not found");
        await _categoryRepository.RemoveAsync(existing, cancellationToken);
    }

    private async Task EnsureUniqueAsync(Category category, CancellationToken cancellationToken, Guid? currentId = null)
    {
        var nameExists = await _categoryRepository.NameExistsAsync(category.Name, cancellationToken);
        if (nameExists && currentId != category.Id)
        {
            throw new ValidationException("Category name already exists");
        }
    }

    private static void EnsureAdmin(RoleType role)
    {
        if (role is RoleType.Admin or RoleType.SuperAdmin)
        {
            return;
        }

        throw new AuthorizationException("Role cannot manage categories");
    }
}
