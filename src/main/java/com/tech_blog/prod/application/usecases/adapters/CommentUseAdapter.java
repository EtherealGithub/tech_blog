package com.tech_blog.prod.application.usecases.adapters;

import com.tech_blog.prod.application.dto.requests.comments.CreateCommentRequest;
import com.tech_blog.prod.application.dto.responses.comments.CommentResponse;
import com.tech_blog.prod.application.usecases.ports.ICommentUsePort;
import com.tech_blog.prod.domain.services.ports.ICommentServPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CommentUseAdapter implements ICommentUsePort {

    private final ICommentServPort iCommentServPort;

    public CommentUseAdapter(ICommentServPort iCommentServPort) {
        this.iCommentServPort = iCommentServPort;
    }

    @Override
    public Page<CommentResponse> paginatedListCommentsByPostId(Long postId, int page, int size) {
        return iCommentServPort.paginatedListCommentsByPostId(postId, page, size);
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        return iCommentServPort.getCommentById(id);
    }

    @Override
    public CommentResponse createComment(CreateCommentRequest commentRequest) {
        return iCommentServPort.createComment(commentRequest);
    }

    @Override
    public void deleteCommentById(Long postId) {
        iCommentServPort.deleteCommentById(postId);
    }
}
