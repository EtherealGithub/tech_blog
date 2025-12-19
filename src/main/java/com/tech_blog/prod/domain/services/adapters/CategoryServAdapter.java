package com.tech_blog.prod.domain.services.adapters;

import com.tech_blog.prod.application.dto.requests.categories.CreateCategoryRequest;
import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;
import com.tech_blog.prod.domain.services.ports.ICategoryServPort;
import com.tech_blog.prod.infrastructure.database.entities.CategoryEntity;
import com.tech_blog.prod.infrastructure.database.repositories.ICategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServAdapter implements ICategoryServPort {

    // Inyección de dependencias
    private final ICategoryRepository iCategoryRepository;

    // Constructor
    public CategoryServAdapter(ICategoryRepository iCategoryRepository) {
        this.iCategoryRepository = iCategoryRepository;
    }

    // Implementación
    @Override
    public List<CategoryResponse> listCategories() {
        return iCategoryRepository.findAll().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return auxiliaryCategoryEntityToResponse(getCategoryEntityById(id));
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest categoryRequest) {
        if (iCategoryRepository.existsByNameIgnoreCase(categoryRequest.name())) {
            throw new IllegalArgumentException("Category already exists: " + categoryRequest.name());
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryRequest.name().trim());
        categoryEntity = iCategoryRepository.save(categoryEntity);
        return new CategoryResponse(categoryEntity.getId(), categoryEntity.getName());
    }

    @Override
    public CategoryEntity getCategoryEntityById(Long id) {
        return iCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
    }


    // Métodos auxiliares
    private CategoryResponse auxiliaryCategoryEntityToResponse(CategoryEntity categoryEntity) {
        return new CategoryResponse(
                categoryEntity.getId(),
                categoryEntity.getName()
        );
    }
}
