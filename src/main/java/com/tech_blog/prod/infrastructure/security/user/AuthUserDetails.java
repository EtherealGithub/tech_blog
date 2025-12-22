package com.tech_blog.prod.infrastructure.security.user;

import com.tech_blog.prod.infrastructure.database.entities.UserEntity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NullMarked
public class AuthUserDetails implements UserDetails {

    // Inyección de dependencias
    private final UserEntity userEntity;

    // Constructor
    public AuthUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // Helpers
    public Long getUserId() {
        return userEntity.getId();
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    // Implementación
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> auth = new ArrayList<>();

        if (Boolean.TRUE.equals(userEntity.getIsUser()))
            auth.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (Boolean.TRUE.equals(userEntity.getIsAdmin()))
            auth.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (Boolean.TRUE.equals(userEntity.getIsSuperadmin()))
            auth.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));

        return auth;
    }

    @Override
    public @Nullable String getPassword() {
        return userEntity.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
