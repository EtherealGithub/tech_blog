namespace TechBlog.Domain.Ports;

public interface IPasswordHasher
{
    string Hash(string input);
    bool Verify(string input, string hash);
}
