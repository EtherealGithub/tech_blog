package com.tech_blog.prod.application.dto.requests.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest (
   @NotBlank @Size(max = 60) String name
) {}
