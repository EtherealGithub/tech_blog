using TechBlog.Application.DTOs.Users;
using TechBlog.Domain.Enums;

namespace TechBlog.Application.Ports;

public interface IUserUseCase
{
    Task<UserResponse> CreateAsync(UserCreateRequest request, RoleType performerRole, CancellationToken cancellationToken = default);
    Task<UserResponse> UpdateAsync(UserUpdateRequest request, RoleType performerRole, CancellationToken cancellationToken = default);
    Task<IReadOnlyCollection<UserResponse>> ListAsync(CancellationToken cancellationToken = default);
    Task<UserResponse?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default);
}
