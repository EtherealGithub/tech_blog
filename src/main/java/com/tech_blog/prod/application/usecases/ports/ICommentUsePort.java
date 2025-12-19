package com.tech_blog.prod.application.usecases.ports;

import com.tech_blog.prod.application.dto.requests.comments.CreateCommentRequest;
import com.tech_blog.prod.application.dto.responses.comments.CommentResponse;
import com.tech_blog.prod.infrastructure.database.entities.CommentEntity;
import org.springframework.data.domain.Page;

public interface ICommentUsePort {
    Page<CommentResponse> paginatedListCommentsByPostId(Long postId, int page, int size);
    CommentResponse getCommentById(Long id);
    CommentResponse createComment(CreateCommentRequest commentRequest);
    void deleteCommentById(Long postId);
}
