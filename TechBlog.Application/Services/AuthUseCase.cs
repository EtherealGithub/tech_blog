using TechBlog.Application.DTOs.Auth;
using TechBlog.Application.Ports;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Services;

namespace TechBlog.Application.Services;

public class AuthUseCase : IAuthUseCase
{
    private readonly IUserDomainService _userDomainService;
    private readonly ITokenProvider _tokenProvider;
    private readonly TimeSpan _tokenLifetime;

    public AuthUseCase(IUserDomainService userDomainService, ITokenProvider tokenProvider, TimeSpan tokenLifetime)
    {
        _userDomainService = userDomainService;
        _tokenProvider = tokenProvider;
        _tokenLifetime = tokenLifetime;
    }

    public async Task<LoginResponse> LoginAsync(LoginRequest request, CancellationToken cancellationToken = default)
    {
        var user = await _userDomainService.ValidateCredentialsAsync(request.UsernameOrEmail, request.Password, cancellationToken);
        var expires = DateTime.UtcNow.Add(_tokenLifetime);
        var token = _tokenProvider.GenerateToken(user, expires);
        return new LoginResponse(token, expires, user.Username, user.Role);
    }
}
