using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Api.Extensions;
using TechBlog.Application.DTOs.Users;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/user")]
[Authorize(Roles = "Admin,SuperAdmin")]
public class UserController : ControllerBase
{
    private readonly IUserUseCase _userUseCase;

    public UserController(IUserUseCase userUseCase)
    {
        _userUseCase = userUseCase;
    }

    [HttpGet]
    public async Task<IActionResult> GetUsers(CancellationToken cancellationToken)
    {
        var users = await _userUseCase.ListAsync(cancellationToken);
        return Ok(users);
    }

    [HttpGet("{id:guid}")]
    public async Task<IActionResult> GetById(Guid id, CancellationToken cancellationToken)
    {
        var user = await _userUseCase.GetByIdAsync(id, cancellationToken);
        return user is null ? NotFound() : Ok(user);
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] UserCreateRequest request, CancellationToken cancellationToken)
    {
        var role = User.GetUserRole();
        var response = await _userUseCase.CreateAsync(request, role, cancellationToken);
        return CreatedAtAction(nameof(GetById), new { id = response.Id }, response);
    }

    [HttpPut("{id:guid}")]
    public async Task<IActionResult> Update(Guid id, [FromBody] UserUpdateRequest request, CancellationToken cancellationToken)
    {
        var role = User.GetUserRole();
        request.Id = id;
        var response = await _userUseCase.UpdateAsync(request, role, cancellationToken);
        return Ok(response);
    }
}
