package com.tech_blog.prod.infrastructure.controllers;

import com.tech_blog.prod.application.dto.requests.posts.CreatePostRequest;
import com.tech_blog.prod.application.dto.requests.posts.UpdatePostRequest;
import com.tech_blog.prod.application.dto.responses.posts.PostResponse;
import com.tech_blog.prod.application.usecases.ports.IPostUsePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/post")
public class PostController {

    // Inyeccion de dependencias
    private final IPostUsePort iPostUsePort;

    // Constructor
    public PostController(IPostUsePort iPostUsePort) {
        this.iPostUsePort = iPostUsePort;
    }

    // Endpoints
    @GetMapping("/list_posts")
    public ResponseEntity<Page<PostResponse>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "recent") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(iPostUsePort.paginatedListPostsByCategoryId(categoryId, sort, page, size));
    }

    @GetMapping("/read_post/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(iPostUsePort.getPostById(id));
    }

    @PostMapping("/create_post")
    public ResponseEntity<PostResponse> create(@RequestBody @Valid CreatePostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iPostUsePort.createPost(postRequest));
    }

    @PutMapping("/update_post/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @RequestBody @Valid UpdatePostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(iPostUsePort.updatePost(id, postRequest));
    }

    @DeleteMapping("delete_post/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        iPostUsePort.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
