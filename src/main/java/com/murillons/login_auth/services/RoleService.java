package com.murillons.login_auth.services;

import com.murillons.login_auth.entities.Role;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<Role> registerRole(Role role);
}