package com.tech_blog.prod.domain.services.adapters;

import com.tech_blog.prod.application.dto.requests.comments.CreateCommentRequest;
import com.tech_blog.prod.application.dto.responses.categories.CategoryResponse;
import com.tech_blog.prod.application.dto.responses.comments.CommentResponse;
import com.tech_blog.prod.domain.services.ports.ICommentServPort;
import com.tech_blog.prod.domain.services.ports.IPostServPort;
import com.tech_blog.prod.domain.services.ports.IUserServPort;
import com.tech_blog.prod.infrastructure.database.entities.CommentEntity;
import com.tech_blog.prod.infrastructure.database.entities.PostEntity;
import com.tech_blog.prod.infrastructure.database.repositories.ICommentRepository;
import com.tech_blog.prod.infrastructure.database.repositories.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServAdapter implements ICommentServPort {

    // Inyeccion de dependencias
    private final ICommentRepository iCommentRepository;
    private final IUserServPort iUserServPort;
    private final IPostServPort iPostServPort;

    // Constructor
    public CommentServAdapter(ICommentRepository iCommentRepository, IUserServPort iUserServPort, IPostServPort iPostServPort) {
        this.iCommentRepository = iCommentRepository;
        this.iUserServPort = iUserServPort;
        this.iPostServPort = iPostServPort;
    }

    // Implementación
    @Override
    public Page<CommentResponse> paginatedListCommentsByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return iCommentRepository.findByPost_Id(postId, pageable).map(this::auxiliaryCommentEntityToResponse);
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        return auxiliaryCommentEntityToResponse(getCommentEntityById(id));
    }

    @Override
    public CommentResponse createComment(CreateCommentRequest commentRequest) {
        var post = iPostServPort.getPostEntityById(commentRequest.postId());
        var user = iUserServPort.getUserEntityById(commentRequest.userId());

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentRequest.content().trim());
        commentEntity.setPost(post);
        commentEntity.setUser(user);
        commentEntity.setCreatedAt(LocalDateTime.now());

        commentEntity = iCommentRepository.save(commentEntity);
        return auxiliaryCommentEntityToResponse(commentEntity);
    }

    @Override
    public void deleteCommentById(Long commentId) {
        CommentEntity commentEntity = getCommentEntityById(commentId);
        iCommentRepository.delete(commentEntity);
    }

    @Override
    public CommentEntity getCommentEntityById(Long id) {
        return iCommentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found: " + id));
    }

    // Métodos auxiliares
    private CommentResponse auxiliaryCommentEntityToResponse(CommentEntity commentEntity) {
        return new CommentResponse(
                commentEntity.getId(),
                commentEntity.getContent(),
                commentEntity.getCreatedAt(),
                commentEntity.getPost().getId(),
                commentEntity.getUser().getId(),
                commentEntity.getUser().getUsername()
        );
    }
}
