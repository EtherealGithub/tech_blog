package com.tech_blog.prod.application.usecases.ports;

import com.tech_blog.prod.application.dto.requests.categories.CreateCategoryRequest;
import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;

import java.util.List;

public interface ICategoryUsePort {
    List<CategoryResponse> listCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CreateCategoryRequest categoryRequest);
}
