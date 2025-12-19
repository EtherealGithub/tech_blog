package com.tech_blog.prod.infrastructure.database.repositories;

import com.tech_blog.prod.infrastructure.database.entities.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByPost_Id(Long postId, Pageable pageable);
}
