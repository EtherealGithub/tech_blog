using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using TechBlog.Api.Extensions;
using TechBlog.Application.DTOs.Categories;
using TechBlog.Application.Ports;

namespace TechBlog.Api.Controllers;

[ApiController]
[Route("api/category")]
[Tags("category-controller")]
public class CategoryController : ControllerBase
{
    private readonly ICategoryUseCase _categoryUseCase;

    public CategoryController(ICategoryUseCase categoryUseCase)
    {
        _categoryUseCase = categoryUseCase;
    }

    [AllowAnonymous]
    [HttpGet("list_categories")]
    public async Task<IActionResult> List(CancellationToken cancellationToken)
    {
        var categories = await _categoryUseCase.ListAsync(cancellationToken);
        return Ok(categories);
    }

    [Authorize(Roles = "Admin,SuperAdmin")]
    [HttpPost("create_category")]
    public async Task<IActionResult> Create([FromBody] CategoryRequest request, CancellationToken cancellationToken)
    {
        var response = await _categoryUseCase.CreateAsync(request, User.GetUserRole(), cancellationToken);
        return Ok(response);
    }
}
