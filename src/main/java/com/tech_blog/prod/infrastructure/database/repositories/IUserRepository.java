package com.tech_blog.prod.infrastructure.database.repositories;

import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.infrastructure.database.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameIgnoreCase(String username);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);

    List<UserEntity> findByIsUserAndIsAdminAndIsSuperadmin(
            Boolean isUser,
            Boolean isAdmin,
            Boolean isSuperadmin
    );

    Optional<UserEntity> findByIdAndIsUserAndIsAdminAndIsSuperadmin(
            Long id, Boolean isUser, Boolean isAdmin, Boolean isSuperadmin
    );

}
