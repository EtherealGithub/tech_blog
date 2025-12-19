package com.tech_blog.prod.domain.services.ports;


import com.tech_blog.prod.application.dto.requests.categories.CreateCategoryRequest;
import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;
import com.tech_blog.prod.infrastructure.database.entities.CategoryEntity;

import java.util.List;

public interface ICategoryServPort {
    List<CategoryResponse> listCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CreateCategoryRequest categoryRequest);
    CategoryEntity getCategoryEntityById(Long id);
}
