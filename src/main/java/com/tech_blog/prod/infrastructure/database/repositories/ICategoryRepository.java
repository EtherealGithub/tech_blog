package com.tech_blog.prod.infrastructure.database.repositories;

import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;
import com.tech_blog.prod.infrastructure.database.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository  extends JpaRepository<CategoryEntity,Long> {

    Optional<CategoryEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

    CategoryResponse getCategoryEntityById(Long id);
}
