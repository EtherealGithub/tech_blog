using Microsoft.EntityFrameworkCore;
using TechBlog.Domain.Entities;
using TechBlog.Domain.Ports;
using TechBlog.Infrastructure.Entities;
using TechBlog.Infrastructure.Persistence;

namespace TechBlog.Infrastructure.Adapters;

public class CategoryRepository : ICategoryRepository
{
    private readonly TechBlogDbContext _dbContext;

    public CategoryRepository(TechBlogDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task AddAsync(Category category, CancellationToken cancellationToken = default)
    {
        await _dbContext.Categories.AddAsync(ToEntity(category), cancellationToken);
    }

    public async Task UpdateAsync(Category category, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Categories.FirstOrDefaultAsync(x => x.Id == category.Id, cancellationToken);
        if (entity is null)
        {
            throw new KeyNotFoundException("Category not found");
        }

        entity.Name = category.Name;
        entity.Slug = category.Slug;
        entity.Description = category.Description;
        _dbContext.Categories.Update(entity);
    }

    public async Task RemoveAsync(Category category, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Categories.FirstOrDefaultAsync(x => x.Id == category.Id, cancellationToken);
        if (entity is not null)
        {
            _dbContext.Categories.Remove(entity);
        }
    }

    public async Task<Category?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Categories.AsNoTracking().FirstOrDefaultAsync(c => c.Id == id, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<Category?> GetBySlugAsync(string slug, CancellationToken cancellationToken = default)
    {
        var entity = await _dbContext.Categories.AsNoTracking().FirstOrDefaultAsync(c => c.Slug == slug, cancellationToken);
        return entity is null ? null : ToDomain(entity);
    }

    public async Task<bool> NameExistsAsync(string name, CancellationToken cancellationToken = default)
    {
        return await _dbContext.Categories.AsNoTracking().AnyAsync(c => c.Name == name, cancellationToken);
    }

    public async Task<bool> SlugExistsAsync(string slug, CancellationToken cancellationToken = default)
    {
        return await _dbContext.Categories.AsNoTracking().AnyAsync(c => c.Slug == slug, cancellationToken);
    }

    public async Task<IReadOnlyCollection<Category>> ListAsync(CancellationToken cancellationToken = default)
    {
        var categories = await _dbContext.Categories.AsNoTracking().ToListAsync(cancellationToken);
        return categories.Select(ToDomain).ToList();
    }

    private static Category ToDomain(CategoryEntity entity) => new()
    {
        Id = entity.Id,
        Name = entity.Name,
        Slug = entity.Slug,
        Description = entity.Description
    };

    private static CategoryEntity ToEntity(Category category) => new()
    {
        Id = category.Id,
        Name = category.Name,
        Slug = category.Slug,
        Description = category.Description
    };
}
