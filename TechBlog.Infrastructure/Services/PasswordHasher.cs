using System.Security.Cryptography;
using System.Text;
using TechBlog.Domain.Ports;

namespace TechBlog.Infrastructure.Services;

public class PasswordHasher : IPasswordHasher
{
    private const int SaltSize = 16;
    private const int KeySize = 32;
    private const int Iterations = 10000;

    public string Hash(string input)
    {
        var salt = RandomNumberGenerator.GetBytes(SaltSize);
        var hash = Rfc2898DeriveBytes.Pbkdf2(Encoding.UTF8.GetBytes(input), salt, Iterations, HashAlgorithmName.SHA256, KeySize);
        return Convert.ToBase64String(salt) + ":" + Convert.ToBase64String(hash);
    }

    public bool Verify(string input, string hash)
    {
        var parts = hash.Split(':');
        if (parts.Length != 2)
        {
            return false;
        }

        var salt = Convert.FromBase64String(parts[0]);
        var expectedHash = Convert.FromBase64String(parts[1]);
        var actualHash = Rfc2898DeriveBytes.Pbkdf2(Encoding.UTF8.GetBytes(input), salt, Iterations, HashAlgorithmName.SHA256, KeySize);
        return CryptographicOperations.FixedTimeEquals(actualHash, expectedHash);
    }
}
