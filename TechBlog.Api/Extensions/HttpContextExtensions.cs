using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using TechBlog.Domain.Enums;

namespace TechBlog.Api.Extensions;

public static class HttpContextExtensions
{
    public static RoleType GetUserRole(this ClaimsPrincipal principal)
    {
        var role = principal.FindFirstValue(ClaimTypes.Role);
        return Enum.TryParse<RoleType>(role, out var parsed) ? parsed : RoleType.User;
    }

    public static Guid GetUserId(this ClaimsPrincipal principal)
    {
        var subject = principal.FindFirstValue(ClaimTypes.NameIdentifier) ??
                      principal.FindFirstValue(JwtRegisteredClaimNames.Sub);
        return Guid.TryParse(subject ?? string.Empty, out var parsed) ? parsed : Guid.Empty;
    }
}
