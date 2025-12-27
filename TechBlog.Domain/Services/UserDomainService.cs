using System.Linq;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Exceptions;
using TechBlog.Domain.Ports;

namespace TechBlog.Domain.Services;

public interface IUserDomainService
{
    Task<User> RegisterAsync(User user, RoleType performedBy, CancellationToken cancellationToken = default);
    Task<User> UpdateAsync(User user, RoleType performedBy, CancellationToken cancellationToken = default);
    Task<User> ValidateCredentialsAsync(string usernameOrEmail, string password, CancellationToken cancellationToken = default);
}

public class UserDomainService : IUserDomainService
{
    private readonly IUserRepository _userRepository;
    private readonly IPasswordHasher _passwordHasher;

    public UserDomainService(IUserRepository userRepository, IPasswordHasher passwordHasher)
    {
        _userRepository = userRepository;
        _passwordHasher = passwordHasher;
    }

    public async Task<User> RegisterAsync(User user, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        user.Username = user.Username.Trim();
        user.Email = user.Email.Trim();

        NormalizeAndValidateRoles(user);
        await EnsureRoleCanBeManaged(performedBy, user.GetHighestRole());
        await EnsureUserUniqueAsync(user, cancellationToken);

        user.Id = user.Id == Guid.Empty ? Guid.NewGuid() : user.Id;
        user.CreatedAt = DateTime.UtcNow;
        user.PasswordHash = _passwordHasher.Hash(user.PasswordHash);

        await _userRepository.AddAsync(user, cancellationToken);
        return user;
    }

    public async Task<User> UpdateAsync(User user, RoleType performedBy, CancellationToken cancellationToken = default)
    {
        user.Username = user.Username.Trim();
        user.Email = user.Email.Trim();
        NormalizeAndValidateRoles(user);
        await EnsureRoleCanBeManaged(performedBy, user.GetHighestRole());
        await EnsureUserUniqueAsync(user, cancellationToken, user.Id);
        await _userRepository.UpdateAsync(user, cancellationToken);
        return user;
    }

    public async Task<User> ValidateCredentialsAsync(string usernameOrEmail, string password, CancellationToken cancellationToken = default)
    {
        var normalized = usernameOrEmail.Trim();
        var user = await _userRepository.GetByUsernameAsync(normalized, cancellationToken) ??
                   await _userRepository.GetByEmailAsync(normalized, cancellationToken);

        if (user is null || !_passwordHasher.Verify(password, user.PasswordHash))
        {
            throw new AuthorizationException("Invalid credentials");
        }

        return user;
    }

    private async Task EnsureUserUniqueAsync(User user, CancellationToken cancellationToken, Guid? existingId = null)
    {
        var usernameTaken = await _userRepository.UsernameExistsAsync(user.Username, cancellationToken);
        if (usernameTaken && existingId != user.Id)
        {
            throw new ValidationException("Username already exists");
        }

        var emailTaken = await _userRepository.EmailExistsAsync(user.Email, cancellationToken);
        if (emailTaken && existingId != user.Id)
        {
            throw new ValidationException("Email already exists");
        }
    }

    private static void NormalizeAndValidateRoles(User user)
    {
        var trueCount = new[] { user.IsSuperAdmin, user.IsAdmin, user.IsUser }.Count(flag => flag);
        if (trueCount == 0)
        {
            user.IsUser = true;
            trueCount = 1;
        }

        if (trueCount > 1)
        {
            throw new ValidationException("User role flags must be mutually exclusive");
        }
    }

    private Task EnsureRoleCanBeManaged(RoleType actorRole, RoleType targetRole)
    {
        if (actorRole == RoleType.User && targetRole == RoleType.User)
        {
            return Task.CompletedTask;
        }

        if (actorRole == RoleType.SuperAdmin)
        {
            return Task.CompletedTask;
        }

        if (actorRole == RoleType.Admin && targetRole == RoleType.User)
        {
            return Task.CompletedTask;
        }

        throw new AuthorizationException("Insufficient role to manage target user");
    }
}
