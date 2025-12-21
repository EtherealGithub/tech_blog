package com.tech_blog.prod.application.dto.requests.users;

import com.tech_blog.prod.application.constants.Role;

public record UpdateUserRoleRequest(
        Role role
) {
}
