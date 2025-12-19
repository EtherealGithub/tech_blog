package com.tech_blog.prod.infrastructure.security.user;

import com.tech_blog.prod.infrastructure.database.repositories.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Inyección de dependencias
    private final IUserRepository iUserRepository;

    // Constructor
    public CustomUserDetailsService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    // Implementación
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = iUserRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException( "User not found: " + username));

        return new AuthUserDetails(user);
    }
}
