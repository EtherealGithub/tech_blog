package com.tech_blog.prod.infrastructure.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"posts", "comments"})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable=false, length=50, unique=true)
    private String username;

    @Column(nullable=false, length=120, unique=true)
    private String email;

    @Column(name="password_hash", nullable=false, length=255)
    private String passwordHash;

    @Column(name="is_user", nullable=false)
    private Boolean isUser = true;

    @Column(name="is_admin", nullable=false)
    private Boolean isAdmin = false;

    @Column(name="is_superadmin", nullable=false)
    private Boolean isSuperadmin = false;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;


    // Relations
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();


    // Helpers
    public boolean isSuper() {
        return Boolean.TRUE.equals(isSuperadmin);
    }

    public boolean isAdminOrHigher() {
        return Boolean.TRUE.equals(isAdmin) || Boolean.TRUE.equals(isSuperadmin);
    }
}
