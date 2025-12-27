using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Api.Extensions;
using TechBlog.Application.DTOs.Comments;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/comment")]
[Tags("comment-controller")]
public class CommentController : ControllerBase
{
    private readonly ICommentUseCase _commentUseCase;

    public CommentController(ICommentUseCase commentUseCase)
    {
        _commentUseCase = commentUseCase;
    }

    [AllowAnonymous]
    [HttpGet("list_comments_by_post")]
    public async Task<IActionResult> ListByQuery([FromQuery] Guid postId, CancellationToken cancellationToken)
    {
        var comments = await _commentUseCase.ListByPostAsync(postId, cancellationToken);
        return Ok(comments);
    }

    [AllowAnonymous]
    [HttpGet("read_comment/{id:guid}")]
    public async Task<IActionResult> GetById(Guid id, CancellationToken cancellationToken)
    {
        var comment = await _commentUseCase.GetByIdAsync(id, cancellationToken);
        return comment is null ? NotFound() : Ok(comment);
    }

    [Authorize]
    [HttpPost("create_comment")]
    public async Task<IActionResult> Create([FromBody] CommentRequest request, CancellationToken cancellationToken)
    {
        var response = await _commentUseCase.CreateAsync(request, User.GetUserId(), User.GetUserRole(), cancellationToken);
        return Ok(response);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpDelete("delete_comment/{id:guid}")]
    public async Task<IActionResult> Delete(Guid id, CancellationToken cancellationToken)
    {
        await _commentUseCase.DeleteAsync(id, User.GetUserRole(), cancellationToken);
        return NoContent();
    }
}
