package com.tech_blog.prod;

import com.tech_blog.prod.infrastructure.database.entities.UserEntity;
import com.tech_blog.prod.infrastructure.database.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SuperAdminBootstrapping implements ApplicationRunner {

    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${app.bootstrapping.superadmin.username}")
    private String username;

    @Value("${app.bootstrapping.superadmin.email}")
    private String email;

    @Value("${app.bootstrapping.superadmin.password}")
    private String password;

    public SuperAdminBootstrapping(IUserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (userRepository.existsByUsernameIgnoreCase(username) || userRepository.existsByEmailIgnoreCase(email)) {
            return;
        }

        UserEntity u = new UserEntity();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));

        u.setIsUser(false);
        u.setIsAdmin(false);
        u.setIsSuperadmin(true);

        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(null);

        userRepository.save(u);
    }
}