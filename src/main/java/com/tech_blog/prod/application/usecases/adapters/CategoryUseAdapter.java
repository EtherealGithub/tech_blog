package com.tech_blog.prod.application.usecases.adapters;

import com.tech_blog.prod.application.dto.requests.categories.CreateCategoryRequest;
import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;
import com.tech_blog.prod.application.usecases.ports.ICategoryUsePort;
import com.tech_blog.prod.domain.services.ports.ICategoryServPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryUseAdapter implements ICategoryUsePort {

    // Inyección de dependencias
    private final ICategoryServPort iCategoryServPort;

    // Constructor
    public CategoryUseAdapter(ICategoryServPort iCategoryServPort) {
        this.iCategoryServPort = iCategoryServPort;
    }

    // Implementación
    @Override
    public List<CategoryResponse> listCategories() {
        return iCategoryServPort.listCategories();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return iCategoryServPort.getCategoryById(id);
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest categoryRequest) {
        return iCategoryServPort.createCategory(categoryRequest);
    }
}
