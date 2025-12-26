using TechBlog.Application.DTOs.Categories;
using TechBlog.Domain.Enums;

namespace TechBlog.Application.Ports;

public interface ICategoryUseCase
{
    Task<CategoryResponse> CreateAsync(CategoryRequest request, RoleType performerRole, CancellationToken cancellationToken = default);
    Task<CategoryResponse> UpdateAsync(CategoryRequest request, RoleType performerRole, CancellationToken cancellationToken = default);
    Task DeleteAsync(Guid id, RoleType performerRole, CancellationToken cancellationToken = default);
    Task<CategoryResponse?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default);
    Task<IReadOnlyCollection<CategoryResponse>> ListAsync(CancellationToken cancellationToken = default);
}
