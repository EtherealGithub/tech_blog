using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Application.DTOs.Auth;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/auth")]
public class AuthController : ControllerBase
{
    private readonly IAuthUseCase _authUseCase;

    public AuthController(IAuthUseCase authUseCase)
    {
        _authUseCase = authUseCase;
    }

    [AllowAnonymous]
    [HttpPost("login")]
    public async Task<ActionResult<LoginResponse>> Login([FromBody] LoginRequest request, CancellationToken cancellationToken)
    {
        var response = await _authUseCase.LoginAsync(request, cancellationToken);
        return Ok(response);
    }
}
