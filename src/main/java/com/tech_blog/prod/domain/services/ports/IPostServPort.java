package com.tech_blog.prod.domain.services.ports;

import com.tech_blog.prod.application.dto.requests.posts.CreatePostRequest;
import com.tech_blog.prod.application.dto.requests.posts.UpdatePostRequest;
import com.tech_blog.prod.application.dto.responses.posts.PostResponse;
import com.tech_blog.prod.infrastructure.database.entities.PostEntity;
import org.springframework.data.domain.Page;

public interface IPostServPort {
    Page<PostResponse> paginatedListPostsByCategoryId(Long categoryId, String sort, int page, int size);
    PostResponse getPostById(Long id);
    PostResponse createPost(CreatePostRequest postRequest);
    PostResponse updatePost(Long id, UpdatePostRequest postRequest);
    void deletePost(Long id);
    PostEntity getPostEntityById(Long id);
}
