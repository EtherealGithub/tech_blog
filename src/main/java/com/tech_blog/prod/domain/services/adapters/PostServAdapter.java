package com.tech_blog.prod.domain.services.adapters;

import com.tech_blog.prod.application.dto.requests.posts.CreatePostRequest;
import com.tech_blog.prod.application.dto.requests.posts.UpdatePostRequest;
import com.tech_blog.prod.application.dto.responses.posts.PostResponse;
import com.tech_blog.prod.domain.services.ports.ICategoryServPort;
import com.tech_blog.prod.domain.services.ports.IPostServPort;
import com.tech_blog.prod.domain.services.ports.IUserServPort;
import com.tech_blog.prod.infrastructure.database.entities.PostEntity;
import com.tech_blog.prod.infrastructure.database.repositories.IPostRepository;
import com.tech_blog.prod.infrastructure.database.repositories.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostServAdapter implements IPostServPort {

    // Inyección de dependencias
    private final IPostRepository iPostRepository;

    private final IUserServPort iUserServPort;
    private final ICategoryServPort iCategoryServPort;

    // Constructor
    public PostServAdapter(IPostRepository iPostRepository, IUserServPort iUserServPort, ICategoryServPort iCategoryServPort) {
        this.iPostRepository = iPostRepository;
        this.iUserServPort = iUserServPort;
        this.iCategoryServPort = iCategoryServPort;
    }

    // Implementación
    @Override
    public Page<PostResponse> paginatedListPostsByCategoryId(Long categoryId, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, auxiliaryResolveSort(sort));
        Page<PostEntity> result = (categoryId != null)
                ? iPostRepository.findByCategory_Id(categoryId, pageable)
                : iPostRepository.findAll(pageable);

        return result.map(this::auxiliaryPostEntityToResponse);
    }

    @Override
    public PostResponse getPostById(Long id) {
        return auxiliaryPostEntityToResponse(getPostEntityById(id));
    }

    @Override
    public PostResponse createPost(CreatePostRequest postRequest) {

        var user = iUserServPort.getUserEntityById(postRequest.userId());
        var category = iCategoryServPort.getCategoryEntityById(postRequest.categoryId());

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postRequest.title().trim());
        postEntity.setContent(postRequest.content().trim());
        postEntity.setUser(user);
        postEntity.setCategory(category);
        postEntity.setIsFeatured(postRequest.isFeatured() != null && postRequest.isFeatured());
        postEntity.setCreatedAt(LocalDateTime.now());
        postEntity.setUpdatedAt(null);

        postEntity = iPostRepository.save(postEntity);
        return auxiliaryPostEntityToResponse(postEntity);
    }

    @Override
    public PostResponse updatePost(Long id, UpdatePostRequest postRequest) {
        PostEntity postEntity = getPostEntityById(id);
        postEntity.setTitle(postRequest.title().trim());
        postEntity.setContent(postRequest.content().trim());
        if (postRequest.isFeatured() != null)
            postEntity.setIsFeatured(postRequest.isFeatured());
        postEntity.setUpdatedAt(LocalDateTime.now());

        postEntity = iPostRepository.save(postEntity);
        return auxiliaryPostEntityToResponse(postEntity);
    }

    @Override
    public void deletePost(Long id) {
        PostEntity postEntity = getPostEntityById(id);
        iPostRepository.delete(postEntity);
    }

    @Override
    public PostEntity getPostEntityById(Long id) {
        return iPostRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found: " + id));
    }

    // Métodos auxiliares
    private Sort auxiliaryResolveSort(String sort) {
        if (sort == null || sort.isBlank() || sort.equalsIgnoreCase("recent")) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        if (sort.equalsIgnoreCase("featured")) {
            return Sort.by(Sort.Direction.DESC, "isFeatured").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

    private PostResponse auxiliaryPostEntityToResponse(PostEntity postEntity) {
        return new PostResponse(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getIsFeatured(),
                postEntity.getCreatedAt(),
                postEntity.getUpdatedAt(),
                postEntity.getUser().getId(),
                postEntity.getUser().getUsername(),
                postEntity.getCategory().getId(),
                postEntity.getCategory().getName()
        );
    }
}
