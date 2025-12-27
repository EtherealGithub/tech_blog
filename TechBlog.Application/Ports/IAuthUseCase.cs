using TechBlog.Application.DTOs.Auth;
using TechBlog.Application.DTOs.Users;

namespace TechBlog.Application.Ports;

public interface IAuthUseCase
{
    Task<LoginResponse> LoginAsync(LoginRequest request, CancellationToken cancellationToken = default);
    Task<UserResponse> RegisterAsync(RegisterRequest request, CancellationToken cancellationToken = default);
}
