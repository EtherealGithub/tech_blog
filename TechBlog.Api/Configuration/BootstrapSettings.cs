namespace TechBlog.Api.Configuration;

public class BootstrapSettings
{
    public SuperadminSettings Superadmin { get; set; } = new();
}

public class SuperadminSettings
{
    public string Username { get; set; } = "superadmin";
    public string Email { get; set; } = "superadmin@example.com";
    public string Password { get; set; } = "ChangeMe123!";
}
