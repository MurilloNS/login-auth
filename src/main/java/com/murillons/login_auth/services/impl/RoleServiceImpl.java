package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.entities.Role;
import com.murillons.login_auth.repositories.RoleRepository;
import com.murillons.login_auth.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> registerRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Role newRole = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }
}