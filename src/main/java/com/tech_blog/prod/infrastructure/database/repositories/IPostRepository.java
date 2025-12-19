package com.tech_blog.prod.infrastructure.database.repositories;

import com.tech_blog.prod.infrastructure.database.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepository  extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findByCategory_Id(Long categoryId, Pageable pageable);
    Page<PostEntity> findByIsFeaturedTrue(Pageable pageable);
}
