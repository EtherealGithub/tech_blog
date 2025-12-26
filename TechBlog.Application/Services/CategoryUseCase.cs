using TechBlog.Application.DTOs.Categories;
using TechBlog.Application.Ports;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Ports;
using TechBlog.Domain.Services;

namespace TechBlog.Application.Services;

public class CategoryUseCase : ICategoryUseCase
{
    private readonly ICategoryDomainService _categoryDomainService;
    private readonly ICategoryRepository _categoryRepository;
    private readonly IUnitOfWork _unitOfWork;

    public CategoryUseCase(ICategoryDomainService categoryDomainService, ICategoryRepository categoryRepository, IUnitOfWork unitOfWork)
    {
        _categoryDomainService = categoryDomainService;
        _categoryRepository = categoryRepository;
        _unitOfWork = unitOfWork;
    }

    public async Task<CategoryResponse> CreateAsync(CategoryRequest request, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        var category = new Category
        {
            Name = request.Name,
            Slug = request.Slug,
            Description = request.Description
        };

        var created = await _categoryDomainService.CreateAsync(category, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(created);
    }

    public async Task<CategoryResponse> UpdateAsync(CategoryRequest request, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        if (request.Id is null)
        {
            throw new ArgumentException("Id is required");
        }

        var existing = await _categoryRepository.GetByIdAsync(request.Id.Value, cancellationToken) ?? throw new KeyNotFoundException("Category not found");
        existing.Name = request.Name;
        existing.Slug = request.Slug;
        existing.Description = request.Description;

        var updated = await _categoryDomainService.UpdateAsync(existing, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(updated);
    }

    public async Task DeleteAsync(Guid id, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        await _categoryDomainService.DeleteAsync(id, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
    }

    public async Task<CategoryResponse?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var category = await _categoryRepository.GetByIdAsync(id, cancellationToken);
        return category is null ? null : Map(category);
    }

    public async Task<IReadOnlyCollection<CategoryResponse>> ListAsync(CancellationToken cancellationToken = default)
    {
        var categories = await _categoryRepository.ListAsync(cancellationToken);
        return categories.Select(Map).ToList();
    }

    private static CategoryResponse Map(Category category) => new(category.Id, category.Name, category.Slug, category.Description);
}
