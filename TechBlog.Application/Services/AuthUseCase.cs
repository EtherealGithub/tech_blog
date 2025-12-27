using TechBlog.Application.DTOs.Auth;
using TechBlog.Application.DTOs.Users;
using TechBlog.Application.Ports;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Ports;
using TechBlog.Domain.Services;

namespace TechBlog.Application.Services;

public class AuthUseCase : IAuthUseCase
{
    private readonly IUserDomainService _userDomainService;
    private readonly IUnitOfWork _unitOfWork;
    private readonly ITokenProvider _tokenProvider;
    private readonly TimeSpan _tokenLifetime;

    public AuthUseCase(
        IUserDomainService userDomainService,
        IUnitOfWork unitOfWork,
        ITokenProvider tokenProvider,
        TimeSpan tokenLifetime)
    {
        _userDomainService = userDomainService;
        _unitOfWork = unitOfWork;
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

    public async Task<UserResponse> RegisterAsync(RegisterRequest request, CancellationToken cancellationToken = default)
    {
        var user = new User
        {
            Username = request.Username,
            Email = request.Email,
            PasswordHash = request.Password,
            Role = RoleType.User
        };

        var created = await _userDomainService.RegisterAsync(user, RoleType.User, cancellationToken);
        await _unitOfWork.SaveChangesAsync(cancellationToken);
        return new UserResponse(created.Id, created.Username, created.Email, created.Role, created.CreatedAt);
    }
}
