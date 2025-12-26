using Microsoft.Extensions.Options;
using TechBlog.Api.Configuration;
using TechBlog.Application.DTOs.Users;
using TechBlog.Application.Ports;
using TechBlog.Domain.Enums;

namespace TechBlog.Api.Services;

public class SuperAdminSeeder : IHostedService
{
    private readonly IServiceProvider _serviceProvider;
    private readonly BootstrapSettings _bootstrapSettings;

    public SuperAdminSeeder(IServiceProvider serviceProvider, IOptions<BootstrapSettings> bootstrapOptions)
    {
        _serviceProvider = serviceProvider;
        _bootstrapSettings = bootstrapOptions.Value;
    }

    public async Task StartAsync(CancellationToken cancellationToken)
    {
        using var scope = _serviceProvider.CreateScope();
        var userUseCase = scope.ServiceProvider.GetRequiredService<IUserUseCase>();
        var existing = await userUseCase.ListAsync(cancellationToken);
        if (existing.Any(u => u.Username.Equals(_bootstrapSettings.Superadmin.Username, StringComparison.OrdinalIgnoreCase)))
        {
            return;
        }

        var request = new UserCreateRequest
        {
            Username = _bootstrapSettings.Superadmin.Username,
            Email = _bootstrapSettings.Superadmin.Email,
            Password = _bootstrapSettings.Superadmin.Password,
            Role = RoleType.SuperAdmin
        };

        await userUseCase.CreateAsync(request, RoleType.SuperAdmin, cancellationToken);
    }

    public Task StopAsync(CancellationToken cancellationToken) => Task.CompletedTask;
}
