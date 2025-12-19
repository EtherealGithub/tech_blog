package com.tech_blog.prod.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @Column(nullable=false, length=1000)
    private String content;

    @ManyToOne(optional=false)
    @JoinColumn(name="post_id")
    private PostEntity post;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;
}
