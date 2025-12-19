package com.tech_blog.prod.infrastructure.controllers;

import com.tech_blog.prod.application.dto.requests.comments.CreateCommentRequest;
import com.tech_blog.prod.application.dto.responses.comments.CommentResponse;
import com.tech_blog.prod.application.usecases.ports.ICommentUsePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final ICommentUsePort iCommentUsePort;

    public CommentController(ICommentUsePort iCommentUsePort) {
        this.iCommentUsePort = iCommentUsePort;
    }

    @GetMapping("/list_comments_by_post")
    public ResponseEntity<Page<CommentResponse>> listCommentsByPostId(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(iCommentUsePort.paginatedListCommentsByPostId(postId, page, size));
    }

    @GetMapping("read_comment/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(iCommentUsePort.getCommentById(id));
    }

    @PostMapping("/create_comment")
    public ResponseEntity<CommentResponse> createComment(@RequestBody @Valid CreateCommentRequest request) {
        CommentResponse created = iCommentUsePort.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/delete_comment/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        iCommentUsePort.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }


}
