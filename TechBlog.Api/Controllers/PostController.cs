using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Api.Extensions;
using TechBlog.Application.DTOs.Posts;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/post")]
[Tags("post-controller")]
public class PostController : ControllerBase
{
    private readonly IPostUseCase _postUseCase;

    public PostController(IPostUseCase postUseCase)
    {
        _postUseCase = postUseCase;
    }

    [AllowAnonymous]
    [HttpGet("list_posts")]
    public async Task<IActionResult> List([FromQuery] Guid? categoryId, [FromQuery] string? sort, [FromQuery] int? page, [FromQuery] int? size, CancellationToken cancellationToken)
    {
        var posts = await _postUseCase.ListAsync(cancellationToken);
        return Ok(posts);
    }

    [AllowAnonymous]
    [HttpGet("read_post/{id:guid}")]
    public async Task<IActionResult> Get(Guid id, CancellationToken cancellationToken)
    {
        var post = await _postUseCase.GetByIdAsync(id, cancellationToken);
        return post is null ? NotFound() : Ok(post);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPost("create_post")]
    public async Task<IActionResult> Create([FromBody] PostRequest request, CancellationToken cancellationToken)
    {
        var response = await _postUseCase.CreateAsync(request, User.GetUserId(), User.GetUserRole(), cancellationToken);
        return Ok(response);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPut("update_post/{id:guid}")]
    public async Task<IActionResult> Update(Guid id, [FromBody] PostRequest request, CancellationToken cancellationToken)
    {
        request.Id = id;
        var response = await _postUseCase.UpdateAsync(request, User.GetUserRole(), cancellationToken);
        return Ok(response);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpDelete("delete_post/{id:guid}")]
    public async Task<IActionResult> Delete(Guid id, CancellationToken cancellationToken)
    {
        await _postUseCase.DeleteAsync(id, User.GetUserRole(), cancellationToken);
        return NoContent();
    }
}
