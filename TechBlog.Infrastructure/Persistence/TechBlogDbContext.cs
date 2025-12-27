using Microsoft.EntityFrameworkCore;
using TechBlog.Infrastructure.Entities;

namespace TechBlog.Infrastructure.Persistence;

public class TechBlogDbContext : DbContext
{
    public TechBlogDbContext(DbContextOptions<TechBlogDbContext> options) : base(options)
    {
    }

    public DbSet<UserEntity> Users => Set<UserEntity>();
    public DbSet<PostEntity> Posts => Set<PostEntity>();
    public DbSet<CategoryEntity> Categories => Set<CategoryEntity>();
    public DbSet<CommentEntity> Comments => Set<CommentEntity>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.HasCharSet("utf8mb4");

        modelBuilder.Entity<UserEntity>(entity =>
        {
            entity.ToTable("users");
            entity.HasKey(e => e.Id);
            entity.Property(e => e.Username).HasMaxLength(80).IsRequired();
            entity.Property(e => e.Email).HasMaxLength(255).IsRequired();
            entity.Property(e => e.PasswordHash).HasMaxLength(512).IsRequired();
            entity.Property(e => e.IsSuperAdmin).IsRequired().HasDefaultValue(false);
            entity.Property(e => e.IsAdmin).IsRequired().HasDefaultValue(false);
            entity.Property(e => e.IsUser).IsRequired().HasDefaultValue(true);
            entity.Property(e => e.CreatedAt).HasDefaultValueSql("CURRENT_TIMESTAMP");
            entity.HasIndex(e => e.Username).IsUnique();
            entity.HasIndex(e => e.Email).IsUnique();
        });

        modelBuilder.Entity<CategoryEntity>(entity =>
        {
            entity.ToTable("categories");
            entity.HasKey(e => e.Id);
            entity.Property(e => e.Name).HasMaxLength(120).IsRequired();
            entity.Property(e => e.Description).HasMaxLength(512);
            entity.HasIndex(e => e.Name).IsUnique();
        });

        modelBuilder.Entity<PostEntity>(entity =>
        {
            entity.ToTable("posts");
            entity.HasKey(e => e.Id);
            entity.Property(e => e.Title).HasMaxLength(200).IsRequired();
            entity.Property(e => e.Name).HasMaxLength(200).IsRequired();
            entity.Property(e => e.Content).HasColumnType("longtext");
            entity.Property(e => e.Featured).HasDefaultValue(false);
            entity.Property(e => e.CreatedAt).HasDefaultValueSql("CURRENT_TIMESTAMP");
            entity.HasOne(e => e.Category).WithMany(c => c.Posts).HasForeignKey(e => e.CategoryId);
            entity.HasOne(e => e.Author).WithMany(u => u.Posts).HasForeignKey(e => e.AuthorId);
            entity.HasIndex(e => e.Name).IsUnique();
            entity.HasIndex(e => e.CreatedAt);
            entity.HasIndex(e => e.Featured);
        });

        modelBuilder.Entity<CommentEntity>(entity =>
        {
            entity.ToTable("comments");
            entity.HasKey(e => e.Id);
            entity.Property(e => e.Content).HasMaxLength(1024).IsRequired();
            entity.Property(e => e.CreatedAt).HasDefaultValueSql("CURRENT_TIMESTAMP");
            entity.HasOne(e => e.Post).WithMany(p => p.Comments).HasForeignKey(e => e.PostId);
            entity.HasOne(e => e.Author).WithMany(u => u.Comments).HasForeignKey(e => e.AuthorId);
            entity.HasIndex(e => e.PostId);
            entity.HasIndex(e => e.AuthorId);
        });
    }
}
