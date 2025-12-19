package com.tech_blog.prod.infrastructure.controllers;

import com.tech_blog.prod.application.dto.requests.categories.CreateCategoryRequest;
import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;
import com.tech_blog.prod.application.usecases.ports.ICategoryUsePort;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    // Inyección de dependencias
    private final ICategoryUsePort iCategoryUsePort;

    // Constructor
    public CategoryController(ICategoryUsePort iCategoryUsePort) {
        this.iCategoryUsePort = iCategoryUsePort;
    }

    // Endpoints
    @GetMapping("/list_categories")
    public ResponseEntity<List<CategoryResponse>> listCategories() {
        List<CategoryResponse> categories = iCategoryUsePort.listCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @PostMapping("/create_category")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iCategoryUsePort.createCategory(createCategoryRequest));
    }
}
