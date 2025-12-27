using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Application.DTOs.Auth;
using TechBlog.Application.DTOs.Users;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/public")]
public class PublicController : ControllerBase
{
    private readonly IPostUseCase _postUseCase;
    private readonly ICategoryUseCase _categoryUseCase;
    private readonly IAuthUseCase _authUseCase;

    public PublicController(IPostUseCase postUseCase, ICategoryUseCase categoryUseCase, IAuthUseCase authUseCase)
    {
        _postUseCase = postUseCase;
        _categoryUseCase = categoryUseCase;
        _authUseCase = authUseCase;
    }

    [AllowAnonymous]
    [HttpGet("posts")]
    public async Task<IActionResult> GetPosts(CancellationToken cancellationToken)
    {
        var posts = await _postUseCase.ListAsync(cancellationToken);
        return Ok(posts);
    }

    [AllowAnonymous]
    [HttpGet("categories")]
    public async Task<IActionResult> GetCategories(CancellationToken cancellationToken)
    {
        var categories = await _categoryUseCase.ListAsync(cancellationToken);
        return Ok(categories);
    }

    [AllowAnonymous]
    [HttpPost("own_register_user")]
    public async Task<ActionResult<UserResponse>> Register([FromBody] RegisterRequest request, CancellationToken cancellationToken)
    {
        var response = await _authUseCase.RegisterAsync(request, cancellationToken);
        return Created(string.Empty, response);
    }
}
