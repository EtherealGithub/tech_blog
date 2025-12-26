using TechBlog.Domain.Ports;
using TechBlog.Infrastructure.Persistence;

namespace TechBlog.Infrastructure.Adapters;

public class UnitOfWork : IUnitOfWork
{
    private readonly TechBlogDbContext _dbContext;

    public UnitOfWork(TechBlogDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public Task SaveChangesAsync(CancellationToken cancellationToken = default) => _dbContext.SaveChangesAsync(cancellationToken);
}
