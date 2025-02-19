package com.murillons.login_auth.dto;

import com.murillons.login_auth.entities.Role;

import java.util.Set;

public class UserResponseDto {
    private String name;
    private String email;
    private Set<Role> roles;

    public UserResponseDto() {
    }

    public UserResponseDto(String name, String email, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}