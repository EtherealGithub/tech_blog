using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using TechBlog.Domain.Enums;
using TechBlog.Infrastructure.Persistence;

#nullable disable

namespace TechBlog.Infrastructure.Migrations
{
    [DbContext(typeof(TechBlogDbContext))]
    partial class TechBlogDbContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "8.0.10")
                .HasAnnotation("Relational:MaxIdentifierLength", 64);

            modelBuilder.HasAnnotation("MySql:CharSet", "utf8mb4");

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.CategoryEntity", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("char(36)");

                    b.Property<string>("Description")
                        .IsRequired()
                        .HasMaxLength(512)
                        .HasColumnType("varchar(512)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasMaxLength(120)
                        .HasColumnType("varchar(120)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.Property<string>("Slug")
                        .IsRequired()
                        .HasMaxLength(160)
                        .HasColumnType("varchar(160)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.HasKey("Id");

                    b.HasIndex("Name")
                        .IsUnique();

                    b.HasIndex("Slug")
                        .IsUnique();

                    b.ToTable("categories");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.CommentEntity", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("char(36)");

                    b.Property<Guid>("AuthorId")
                        .HasColumnType("char(36)");

                    b.Property<string>("Content")
                        .IsRequired()
                        .HasMaxLength(1024)
                        .HasColumnType("varchar(1024)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.Property<DateTime>("CreatedAt")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("datetime(6)")
                        .HasDefaultValueSql("CURRENT_TIMESTAMP");

                    b.Property<Guid>("PostId")
                        .HasColumnType("char(36)");

                    b.HasKey("Id");

                    b.HasIndex("AuthorId");

                    b.HasIndex("PostId");

                    b.ToTable("comments");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.PostEntity", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("char(36)");

                    b.Property<Guid>("AuthorId")
                        .HasColumnType("char(36)");

                    b.Property<Guid>("CategoryId")
                        .HasColumnType("char(36)");

                    b.Property<string>("Content")
                        .IsRequired()
                        .HasColumnType("longtext")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.Property<DateTime>("CreatedAt")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("datetime(6)")
                        .HasDefaultValueSql("CURRENT_TIMESTAMP");

                    b.Property<bool>("Featured")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("tinyint(1)")
                        .HasDefaultValue(false);

                    b.Property<string>("Slug")
                        .IsRequired()
                        .HasMaxLength(200)
                        .HasColumnType("varchar(200)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.Property<string>("Title")
                        .IsRequired()
                        .HasMaxLength(200)
                        .HasColumnType("varchar(200)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.HasKey("Id");

                    b.HasIndex("AuthorId");

                    b.HasIndex("CategoryId");

                    b.HasIndex("CreatedAt");

                    b.HasIndex("Featured");

                    b.HasIndex("Slug")
                        .IsUnique();

                    b.ToTable("posts");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.UserEntity", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("char(36)");

                    b.Property<DateTime>("CreatedAt")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("datetime(6)")
                        .HasDefaultValueSql("CURRENT_TIMESTAMP");

                    b.Property<string>("Email")
                        .IsRequired()
                        .HasMaxLength(255)
                        .HasColumnType("varchar(255)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.Property<string>("PasswordHash")
                        .IsRequired()
                        .HasMaxLength(512)
                        .HasColumnType("varchar(512)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.Property<int>("Role")
                        .HasColumnType("int");

                    b.Property<string>("Username")
                        .IsRequired()
                        .HasMaxLength(80)
                        .HasColumnType("varchar(80)")
                        .HasAnnotation("MySql:CharSet", "utf8mb4");

                    b.HasKey("Id");

                    b.HasIndex("Email")
                        .IsUnique();

                    b.HasIndex("Username")
                        .IsUnique();

                    b.ToTable("users");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.CommentEntity", b =>
                {
                    b.HasOne("TechBlog.Infrastructure.Entities.UserEntity", "Author")
                        .WithMany("Comments")
                        .HasForeignKey("AuthorId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("TechBlog.Infrastructure.Entities.PostEntity", "Post")
                        .WithMany("Comments")
                        .HasForeignKey("PostId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Author");

                    b.Navigation("Post");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.PostEntity", b =>
                {
                    b.HasOne("TechBlog.Infrastructure.Entities.UserEntity", "Author")
                        .WithMany("Posts")
                        .HasForeignKey("AuthorId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("TechBlog.Infrastructure.Entities.CategoryEntity", "Category")
                        .WithMany("Posts")
                        .HasForeignKey("CategoryId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Author");

                    b.Navigation("Category");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.CategoryEntity", b =>
                {
                    b.Navigation("Posts");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.PostEntity", b =>
                {
                    b.Navigation("Comments");
                });

            modelBuilder.Entity("TechBlog.Infrastructure.Entities.UserEntity", b =>
                {
                    b.Navigation("Comments");

                    b.Navigation("Posts");
                });
#pragma warning restore 612, 618
        }
    }
}
