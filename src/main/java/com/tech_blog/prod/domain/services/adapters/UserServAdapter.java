package com.tech_blog.prod.domain.services.adapters;

import com.tech_blog.prod.application.constants.Role;
import com.tech_blog.prod.application.dto.requests.auth.RegisterRequest;
import com.tech_blog.prod.application.dto.requests.users.CreateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRequest;
import com.tech_blog.prod.application.dto.requests.users.UpdateUserRoleRequest;
import com.tech_blog.prod.application.dto.responses.users.UserResponse;
import com.tech_blog.prod.domain.services.ports.IAuthServPort;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServAdapter implements IUserServPort {

    // Inyección de dependencias
    private final IUserRepository iUserRepository;
    private final BCryptPasswordEncoder encoder;
    private final IAuthServPort iAuthServPort;

    // Constructor
    public UserServAdapter(IUserRepository iUserRepository, IAuthServPort iAuthServPort,BCryptPasswordEncoder encoder) {
        this.iUserRepository = iUserRepository;
        this.iAuthServPort = iAuthServPort;
        this.encoder = encoder;
    }

    // Implementación
    @Override
    public UserEntity getUserEntityById(Long id) {
        return iUserRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    @Override
    public UserResponse getCurrentUser() {
        return auxiliaryUserEntityToResponse(iAuthServPort.getCurrentUserEntity());
    }

    @Override
    public List<UserResponse> listUsers() {

        Role role = iAuthServPort.getCurrentUserStrongestRole();

        List<UserEntity> users = switch (role) {
            case SUPERADMIN -> iUserRepository.findAll();
            case ADMIN      -> iUserRepository.findByIsUserAndIsAdminAndIsSuperadmin(true, false, false);
            default         -> List.of();
        };

        return users.stream()
                .map(this::auxiliaryUserEntityToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        return auxiliaryUserEntityToResponse(resolveTargetUser(id));
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {

        Role role = iAuthServPort.getCurrentUserStrongestRole();

        if (role == Role.ADMIN && createUserRequest.role() != CreateUserRequest.UserRole.USER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ADMIN can only create USER");
        }

        String username = createUserRequest.username().trim();
        String email    = createUserRequest.email().trim();

        if (iUserRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (iUserRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(createUserRequest.password()));

        user.setIsUser(false);
        user.setIsAdmin(false);
        user.setIsSuperadmin(false);

        switch (createUserRequest.role()) {
            case USER       -> user.setIsUser(true);
            case ADMIN      -> user.setIsAdmin(true);
            case SUPERADMIN -> user.setIsSuperadmin(true);
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);

        user = iUserRepository.save(user);

        return auxiliaryUserEntityToResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {

        UserEntity user = resolveTargetUser(id);

        user.setUsername(updateUserRequest.username());
        user.setEmail(updateUserRequest.email());
        user.setUpdatedAt(LocalDateTime.now());

        return auxiliaryUserEntityToResponse(iUserRepository.save(user));
    }

    @Override
    public void changePasswordById(Long id, String newPassword) {
        UserEntity user = resolveTargetUser(id);

        user.setPasswordHash(encoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        iUserRepository.save(user);
    }

    @Override
    public UserResponse updateUserRoleById(Long id, UpdateUserRoleRequest updateUserRoleRequest) {

        Role actorRole = iAuthServPort.getCurrentUserStrongestRole();
        UserEntity user = resolveTargetUser(id);

        if (actorRole == Role.ADMIN && updateUserRoleRequest.role() != Role.USER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ADMIN can only assign USER role");
        }

        user.setIsUser(false);
        user.setIsAdmin(false);
        user.setIsSuperadmin(false);

        switch (updateUserRoleRequest.role()) {
            case USER       -> user.setIsUser(true);
            case ADMIN      -> user.setIsAdmin(true);
            case SUPERADMIN -> user.setIsSuperadmin(true);
        }

        user.setUpdatedAt(LocalDateTime.now());

        return auxiliaryUserEntityToResponse(iUserRepository.save(user));
    }

    @Override
    public void deleteUserById(Long id) {
        UserEntity user = resolveTargetUser(id);
        iUserRepository.delete(user);
    }


    @Override
    public UserResponse ownRegister(RegisterRequest registerRequest) {

        String username = registerRequest.username().trim();
        String email = registerRequest.email().trim();

        if (iUserRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (iUserRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(registerRequest.password()));

        user.setIsUser(true);
        user.setIsAdmin(false);
        user.setIsSuperadmin(false);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);

        return auxiliaryUserEntityToResponse(iUserRepository.save(user));
    }




    // Helpers
    private UserEntity resolveTargetUser(Long id) {

        Role role = iAuthServPort.getCurrentUserStrongestRole();

        return switch (role) {

            case SUPERADMIN ->
                    iUserRepository.findById(id)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "User not found"
                            ));

            case ADMIN ->
                    iUserRepository
                            .findByIdAndIsUserAndIsAdminAndIsSuperadmin(id, true, false, false)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "User not found"
                            ));

            case USER ->
                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN, "Forbidden"
                    );
        };
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
