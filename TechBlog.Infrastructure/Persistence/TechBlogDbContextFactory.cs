using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Design;

namespace TechBlog.Infrastructure.Persistence;

public class TechBlogDbContextFactory : IDesignTimeDbContextFactory<TechBlogDbContext>
{
    public TechBlogDbContext CreateDbContext(string[] args)
    {
        var optionsBuilder = new DbContextOptionsBuilder<TechBlogDbContext>();
        var connectionString = "Server=localhost;Database=techblog;User=root;Password=Password123!;";
        optionsBuilder.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString));
        return new TechBlogDbContext(optionsBuilder.Options);
    }
}
