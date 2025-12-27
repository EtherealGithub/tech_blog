using TechBlog.Domain.Entities;

namespace TechBlog.Application.Ports;

public interface ITokenProvider
{
    string GenerateToken(User user, DateTime expiresAt);
}
