using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Api.Extensions;
using TechBlog.Application.DTOs.Categories;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/category")]
public class CategoryController : ControllerBase
{
    private readonly ICategoryUseCase _categoryUseCase;

    public CategoryController(ICategoryUseCase categoryUseCase)
    {
        _categoryUseCase = categoryUseCase;
    }

    [AllowAnonymous]
    [HttpGet]
    [HttpGet("list_categories")]
    public async Task<IActionResult> List(CancellationToken cancellationToken)
    {
        var categories = await _categoryUseCase.ListAsync(cancellationToken);
        return Ok(categories);
    }

    [AllowAnonymous]
    [HttpGet("{id:guid}")]
    [HttpGet("read_category/{id:guid}")]
    public async Task<IActionResult> Get(Guid id, CancellationToken cancellationToken)
    {
        var category = await _categoryUseCase.GetByIdAsync(id, cancellationToken);
        return category is null ? NotFound() : Ok(category);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPost]
    [HttpPost("create_category")]
    public async Task<IActionResult> Create([FromBody] CategoryRequest request, CancellationToken cancellationToken)
    {
        var response = await _categoryUseCase.CreateAsync(request, User.GetUserRole(), cancellationToken);
        return CreatedAtAction(nameof(Get), new { id = response.Id }, response);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPut("{id:guid}")]
    [HttpPut("update_category/{id:guid}")]
    public async Task<IActionResult> Update(Guid id, [FromBody] CategoryRequest request, CancellationToken cancellationToken)
    {
        request.Id = id;
        var response = await _categoryUseCase.UpdateAsync(request, User.GetUserRole(), cancellationToken);
        return Ok(response);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpDelete("{id:guid}")]
    [HttpDelete("delete_category/{id:guid}")]
    public async Task<IActionResult> Delete(Guid id, CancellationToken cancellationToken)
    {
        await _categoryUseCase.DeleteAsync(id, User.GetUserRole(), cancellationToken);
        return NoContent();
    }
}
