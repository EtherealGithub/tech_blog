package com.tech_blog.prod.domain.services.adapters;

import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.domain.services.ports.IUserServPort;
import com.tech_blog.prod.infrastructure.database.entities.UserEntity;
import com.tech_blog.prod.infrastructure.database.repositories.IUserRepository;
import com.tech_blog.prod.infrastructure.security.user.AuthUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServAdapter implements IUserServPort {

    // Inyección de dependencias
    private final IUserRepository iUserRepository;
    private final BCryptPasswordEncoder encoder;

    // Constructor
    public UserServAdapter(IUserRepository iUserRepository, BCryptPasswordEncoder encoder) {
        this.iUserRepository = iUserRepository;
        this.encoder = encoder;
    }

    // Implementación
    @Override
    public UserEntity getUserEntityById(Long id) {
        return iUserRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    @Override
    public UserResponse getCurrentUser(Authentication authentication) {
        UserEntity actored = actor(authentication);
        return auxiliaryUserEntityToResponse(actored);
    }

    @Override
    public List<UserResponse> listUsers(Authentication authentication) {
        UserEntity a = actor(authentication);

        if (!isAdmin(a)) {
            throw new IllegalArgumentException("Access denied: only ADMIN+ can list users");
        }

        return iUserRepository.findAll().stream().map(this::auxiliaryUserEntityToResponse).toList();
    }

    @Override
    public UserResponse getUserById(Long id, Authentication authentication) {
        UserEntity a = actor(authentication);
        if (!isAdmin(a)) throw new IllegalArgumentException("Access denied: only ADMIN+");

        UserEntity target = getUserEntityById(id);
        assertCanManageTarget(a, target);

        return auxiliaryUserEntityToResponse(target);
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest, Authentication authentication) {

        UserEntity actored = actor(authentication);

        if (!isAdmin(actored)) {
            throw new IllegalArgumentException("Access denied: only ADMIN+ can create users");
        }

        if (isAdmin(actored) && !isSuper(actored) && createUserRequest.role() != CreateUserRequest.UserRole.USER) {
            throw new IllegalArgumentException("ADMIN can only create USER");
        }

        //if (createUserRequest.role() == CreateUserRequest.UserRole.SUPERADMIN) {
        //    throw new IllegalArgumentException("Creating SUPERADMIN via API is not allowed");
        //}

        String username = createUserRequest.username().trim();
        String email = createUserRequest.email().trim();

        if (iUserRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        if (iUserRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        userEntity.setPasswordHash(encoder.encode(createUserRequest.password()));
        applyRoleFlags(userEntity, createUserRequest.role());
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(null);

        userEntity = iUserRepository.save(userEntity);
        return auxiliaryUserEntityToResponse(userEntity);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest, Authentication authentication) {
        UserEntity actored = actor(authentication);
        if (!isAdmin(actored)) throw new IllegalArgumentException("Access denied: only ADMIN+");

        UserEntity userEntity = getUserEntityById(id);
        assertCanManageTarget(actored, userEntity);

        String newUsername = updateUserRequest.username().trim();
        String newEmail = updateUserRequest.email().trim();

        iUserRepository.findByUsernameIgnoreCase(newUsername)
                .filter(other -> !other.getId().equals(id))
                .ifPresent(x -> { throw new IllegalArgumentException("Username already exists: " + newUsername); });

        iUserRepository.findByEmailIgnoreCase(newEmail)
                .filter(other -> !other.getId().equals(id))
                .ifPresent(x -> { throw new IllegalArgumentException("Email already exists: " + newEmail); });

        userEntity.setUsername(newUsername);
        userEntity.setEmail(newEmail);
        userEntity.setUpdatedAt(LocalDateTime.now());

        userEntity = iUserRepository.save(userEntity);
        return auxiliaryUserEntityToResponse(userEntity);
    }

    @Override
    public void changePasswordById(Long id, String newPassword, Authentication authentication) {
        UserEntity actored = actor(authentication);
        if (!isAdmin(actored)) throw new IllegalArgumentException("Access denied: only ADMIN+");

        UserEntity target = getUserEntityById(id);
        assertCanManageTarget(actored, target);

        target.setPasswordHash(encoder.encode(newPassword));
        target.setUpdatedAt(LocalDateTime.now());
        iUserRepository.save(target);
    }

    @Override
    public void deleteUserById(Long id, Authentication authentication) {
        UserEntity actored = actor(authentication);
        if (!isAdmin(actored)) throw new IllegalArgumentException("Access denied: only ADMIN+");

        UserEntity target = getUserEntityById(id);
        assertCanManageTarget(actored, target);

        if (actored.getId().equals(target.getId())) {
            throw new IllegalArgumentException("You cannot delete yourself");
        }

        iUserRepository.delete(target);
    }



    // Helpers
    private UserEntity actor(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthUserDetails principal)) {
            throw new IllegalArgumentException("Unauthenticated");
        }
        return principal.getUserEntity();
    }

    private boolean isSuper(UserEntity a) {
        return Boolean.TRUE.equals(a.getIsSuperadmin());
    }

    private boolean isAdmin(UserEntity a) {
        return Boolean.TRUE.equals(a.getIsAdmin()) || Boolean.TRUE.equals(a.getIsSuperadmin());
    }

    private boolean targetIsAdminOrSuper(UserEntity target) {
        return Boolean.TRUE.equals(target.getIsAdmin()) || Boolean.TRUE.equals(target.getIsSuperadmin());
    }

    private void assertCanManageTarget(UserEntity actor, UserEntity target) {
        if (isSuper(actor)) return;

        if (!isAdmin(actor)) {
            throw new IllegalArgumentException("Access denied: only ADMIN+ can manage users");
        }

        if (targetIsAdminOrSuper(target)) {
            throw new IllegalArgumentException("Access denied: ADMIN cannot manage ADMIN/SUPERADMIN");
        }
    }

    private void applyRoleFlags(UserEntity u, CreateUserRequest.UserRole role) {
        switch (role) {
            case SUPERADMIN -> { u.setIsSuperadmin(true); u.setIsAdmin(true); u.setIsUser(true); }
            case ADMIN      -> { u.setIsSuperadmin(false); u.setIsAdmin(true); u.setIsUser(true); }
            default         -> { u.setIsSuperadmin(false); u.setIsAdmin(false); u.setIsUser(true); }
        }
    }

    private UserResponse auxiliaryUserEntityToResponse(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getIsUser(),
                userEntity.getIsAdmin(),
                userEntity.getIsSuperadmin(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }
}
