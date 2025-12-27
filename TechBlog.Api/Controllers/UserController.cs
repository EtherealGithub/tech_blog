using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Api.Extensions;
using TechBlog.Application.DTOs.Users;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/user")]
[Authorize]
public class UserController : ControllerBase
{
    private readonly IUserUseCase _userUseCase;

    public UserController(IUserUseCase userUseCase)
    {
        _userUseCase = userUseCase;
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpGet]
    [HttpGet("list_users")]
    public async Task<IActionResult> GetUsers(CancellationToken cancellationToken)
    {
        var users = await _userUseCase.ListAsync(cancellationToken);
        return Ok(users);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpGet("{id:guid}")]
    [HttpGet("read_user/{id:guid}")]
    public async Task<IActionResult> GetById(Guid id, CancellationToken cancellationToken)
    {
        var user = await _userUseCase.GetByIdAsync(id, cancellationToken);
        return user is null ? NotFound() : Ok(user);
    }

    [HttpGet("me")]
    public async Task<IActionResult> Me(CancellationToken cancellationToken)
    {
        var user = await _userUseCase.GetByIdAsync(User.GetUserId(), cancellationToken);
        return user is null ? NotFound() : Ok(user);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPost]
    [HttpPost("create_user")]
    public async Task<IActionResult> Create([FromBody] UserCreateRequest request, CancellationToken cancellationToken)
    {
        var role = User.GetUserRole();
        var response = await _userUseCase.CreateAsync(request, role, cancellationToken);
        return CreatedAtAction(nameof(GetById), new { id = response.Id }, response);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPut("{id:guid}")]
    [HttpPut("update_user/{id:guid}")]
    public async Task<IActionResult> Update(Guid id, [FromBody] UserUpdateRequest request, CancellationToken cancellationToken)
    {
        var role = User.GetUserRole();
        request.Id = id;
        var response = await _userUseCase.UpdateAsync(request, role, cancellationToken);
        return Ok(response);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPatch("update_user_role/{id:guid}")]
    public async Task<IActionResult> UpdateRole(Guid id, [FromBody] UpdateUserRoleRequest request, CancellationToken cancellationToken)
    {
        request.Id = id;
        var response = await _userUseCase.UpdateRoleAsync(request, User.GetUserRole(), cancellationToken);
        return Ok(response);
    }

    [HttpPatch("change_password/{id:guid}")]
    public async Task<IActionResult> ChangePassword(Guid id, [FromBody] ChangePasswordRequest request, CancellationToken cancellationToken)
    {
        request.Id = id;
        await _userUseCase.ChangePasswordAsync(request, User.GetUserId(), User.GetUserRole(), cancellationToken);
        return NoContent();
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpDelete("{id:guid}")]
    [HttpDelete("delete_user/{id:guid}")]
    public async Task<IActionResult> Delete(Guid id, CancellationToken cancellationToken)
    {
        var existing = await _userUseCase.GetByIdAsync(id, cancellationToken);
        if (existing is null)
        {
            return NotFound();
        }

        await _userUseCase.DeleteAsync(id, User.GetUserRole(), User.GetUserId(), cancellationToken);
        return NoContent();
    }
}
