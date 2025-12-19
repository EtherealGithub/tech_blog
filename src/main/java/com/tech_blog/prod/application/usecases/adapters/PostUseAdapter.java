package com.tech_blog.prod.application.usecases.adapters;

import com.tech_blog.prod.application.dto.requests.posts.CreatePostRequest;
import com.tech_blog.prod.application.dto.requests.posts.UpdatePostRequest;
import com.tech_blog.prod.application.dto.responses.posts.PostResponse;
import com.tech_blog.prod.application.usecases.ports.IPostUsePort;
import com.tech_blog.prod.domain.services.ports.IPostServPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PostUseAdapter implements IPostUsePort {

    // Inyección de dependencias
    private final IPostServPort iPostServPort;

    // Constructor
    public PostUseAdapter(IPostServPort iPostServPort) {
        this.iPostServPort = iPostServPort;
    }

    // Implementación
    @Override
    public Page<PostResponse> paginatedListPostsByCategoryId(Long categoryId, String sort, int page, int size) {
        return iPostServPort.paginatedListPostsByCategoryId(categoryId, sort, page, size);
    }

    @Override
    public PostResponse getPostById(Long id) {
        return iPostServPort.getPostById(id);
    }

    @Override
    public PostResponse createPost(CreatePostRequest postRequest) {
        return iPostServPort.createPost(postRequest);
    }

    @Override
    public PostResponse updatePost(Long id, UpdatePostRequest postRequest) {
        return iPostServPort.updatePost(id, postRequest);
    }

    @Override
    public void deletePost(Long id) {
        iPostServPort.deletePost(id);
    }
}
