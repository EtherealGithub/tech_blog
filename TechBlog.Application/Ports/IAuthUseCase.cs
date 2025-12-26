using TechBlog.Application.DTOs.Auth;

namespace TechBlog.Application.Ports;

public interface IAuthUseCase
{
    Task<LoginResponse> LoginAsync(LoginRequest request, CancellationToken cancellationToken = default);
}
