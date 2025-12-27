using TechBlog.Application.DTOs.Users;
using TechBlog.Application.Ports;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Ports;
using TechBlog.Domain.Services;

namespace TechBlog.Application.Services;

public class UserUseCase : IUserUseCase
{
    private readonly IUserDomainService _userDomainService;
    private readonly IUserRepository _userRepository;
    private readonly IUnitOfWork _unitOfWork;

    public UserUseCase(IUserDomainService userDomainService, IUserRepository userRepository, IUnitOfWork unitOfWork)
    {
        _userDomainService = userDomainService;
        _userRepository = userRepository;
        _unitOfWork = unitOfWork;
    }

    public async Task<UserResponse> CreateAsync(UserCreateRequest request, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        var user = new User
        {
            Username = request.Username,
            Email = request.Email,
            PasswordHash = request.Password,
            IsSuperAdmin = request.Role == RoleType.SuperAdmin || request.IsSuperAdmin,
            IsAdmin = request.Role == RoleType.Admin || request.IsAdmin,
            IsUser = request.Role == RoleType.User || request.IsUser
        };

        var created = await _userDomainService.RegisterAsync(user, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(created);
    }

    public async Task<UserResponse> UpdateAsync(UserUpdateRequest request, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        var existing = await _userRepository.GetByIdAsync(request.Id, cancellationToken) ?? throw new KeyNotFoundException("User not found");
        existing.Username = request.Username;
        existing.Email = request.Email;
        // Roles are updated via UpdateRoleAsync
        var updated = await _userDomainService.UpdateAsync(existing, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(updated);
    }

    public async Task<UserResponse> UpdateRoleAsync(UpdateUserRoleRequest request, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        var existing = await _userRepository.GetByIdAsync(request.Id, cancellationToken) ?? throw new KeyNotFoundException("User not found");
        existing.IsSuperAdmin = request.Role == RoleType.SuperAdmin;
        existing.IsAdmin = request.Role == RoleType.Admin;
        existing.IsUser = request.Role == RoleType.User;

        var updated = await _userDomainService.UpdateAsync(existing, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return Map(updated);
    }

    public async Task ChangePasswordAsync(ChangePasswordRequest request, Guid performerId, RoleType performerRole, CancellationToken cancellationToken = default)
    {
        await _userDomainService.ChangePasswordAsync(request.Id, request.NewPassword, performerId, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
    }

    public async Task<IReadOnlyCollection<UserResponse>> ListAsync(CancellationToken cancellationToken = default)
    {
        var users = await _userRepository.GetAllAsync(cancellationToken);
        return users.Select(Map).ToList();
    }

    public async Task<UserResponse?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var user = await _userRepository.GetByIdAsync(id, cancellationToken);
        return user is null ? null : Map(user);
    }

    public async Task DeleteAsync(Guid id, RoleType performerRole, Guid performerId, CancellationToken cancellationToken = default)
    {
        var user = await _userRepository.GetByIdAsync(id, cancellationToken) ?? throw new KeyNotFoundException("User not found");
        await _userDomainService.DeleteAsync(user, performerId, performerRole, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
    }

    private static UserResponse Map(User user) => new(user.Id, user.Username, user.Email, user.IsSuperAdmin, user.IsAdmin, user.IsUser, user.CreatedAt);
}
