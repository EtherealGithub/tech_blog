package com.tech_blog.prod.infrastructure.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"comments"})
public class PostEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @Column(nullable=false, length=150)
    private String title;

    @Lob
    @Column(nullable=false)
    private String content;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne(optional=false)
    @JoinColumn(name="category_id")
    private CategoryEntity category;

    @Column(name="is_featured", nullable=false)
    private Boolean isFeatured = false;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    //
    @OneToMany(
            mappedBy = "post",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CommentEntity> comments = new ArrayList<>();
}
