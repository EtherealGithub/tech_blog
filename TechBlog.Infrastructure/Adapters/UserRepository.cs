using Microsoft.EntityFrameworkCore;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Enums;
using TechBlog.Domain.Ports;
using TechBlog.Infrastructure.Entities;
using TechBlog.Infrastructure.Persistence;

namespace TechBlog.Infrastructure.Adapters;

public class UserRepository : IUserRepository
{
    private readonly TechBlogDbContext _dbContext;

    public UserRepository(TechBlogDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task AddAsync(User user, CancellationToken cancellationToken = default)
    {
        var entity = ToEntity(user);
        await _dbContext.Users.AddAsync(entity, cancellationToken);
    }

    public async Task UpdateAsync(User user, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Users.FirstOrDefaultAsync(x => x.Id == user.Id, cancellationToken);
        if (entity is null)
        {
            throw new KeyNotFoundException("User not found");
        }

        entity.Username = user.Username;
        entity.Email = user.Email;
        entity.Role = user.Role;
        entity.PasswordHash = user.PasswordHash;
        _dbContext.Users.Update(entity);
    }

    public async Task<User?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Id == id, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<User?> GetByUsernameAsync(string username, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Username == username, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<User?> GetByEmailAsync(string email, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Email == email, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<bool> UsernameExistsAsync(string username, CancellationToken cancellationToken = default)
    {
        return await _dbContext.Users.AsNoTracking().AnyAsync(u => u.Username == username, cancellationToken);
    }

    public async Task<bool> EmailExistsAsync(string email, CancellationToken cancellationToken = default)
    {
        return await _dbContext.Users.AsNoTracking().AnyAsync(u => u.Email == email, cancellationToken);
    }

    public async Task<IReadOnlyCollection<User>> GetAllAsync(CancellationToken cancellationToken = default)
    {
        var items = await _dbContext.Users.AsNoTracking().ToListAsync(cancellationToken);
        return items.Select(ToDomain).ToList();
    }

    private static User ToDomain(UserEntity entity) => new()
    {
        Id = entity.Id,
        Username = entity.Username,
        Email = entity.Email,
        PasswordHash = entity.PasswordHash,
        Role = entity.Role,
        CreatedAt = entity.CreatedAt
    };

    private static UserEntity ToEntity(User user) => new()
    {
        Id = user.Id,
        Username = user.Username,
        Email = user.Email,
        PasswordHash = user.PasswordHash,
        Role = user.Role,
        CreatedAt = user.CreatedAt
    };
}
