using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/public")]
public class PublicController : ControllerBase
{
    private readonly IPostUseCase _postUseCase;
    private readonly ICategoryUseCase _categoryUseCase;

    public PublicController(IPostUseCase postUseCase, ICategoryUseCase categoryUseCase)
    {
        _postUseCase = postUseCase;
        _categoryUseCase = categoryUseCase;
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
}
