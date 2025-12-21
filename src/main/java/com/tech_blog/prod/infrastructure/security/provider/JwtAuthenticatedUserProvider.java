package com.tech_blog.prod.infrastructure.security.provider;

import com.tech_blog.prod.application.constants.Role;
import com.tech_blog.prod.domain.services.ports.IAuthServPort;
import com.tech_blog.prod.infrastructure.database.entities.UserEntity;
import com.tech_blog.prod.infrastructure.security.user.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class JwtAuthenticatedUserProvider implements IAuthServPort {

    @Override
    public String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        return auth.getName();
    }

    @Override
    public UserEntity getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof AuthUserDetails principal)) {
            throw new IllegalArgumentException("Unauthenticated");
        }
        return principal.getUserEntity();
    }

    @Override
    public Role getCurrentUserStrongestRole() {
        UserEntity u = getCurrentUserEntity();

        if (Boolean.TRUE.equals(u.getIsSuperadmin())) return Role.SUPERADMIN;
        if (Boolean.TRUE.equals(u.getIsAdmin()))      return Role.ADMIN;
        if (Boolean.TRUE.equals(u.getIsUser()))       return Role.USER;

        throw new IllegalStateException("User has no role flags enabled");
    }

    @Override
    public Set<Role> getCurrentUserRoles() {
        UserEntity u = getCurrentUserEntity();
        EnumSet<Role> roles = EnumSet.noneOf(Role.class);

        if (Boolean.TRUE.equals(u.getIsUser())) roles.add(Role.USER);
        if (Boolean.TRUE.equals(u.getIsAdmin())) roles.add(Role.ADMIN);
        if (Boolean.TRUE.equals(u.getIsSuperadmin())) roles.add(Role.SUPERADMIN);

        return roles;
    }



}
