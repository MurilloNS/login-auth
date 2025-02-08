package com.murillons.login_auth.dto;

import com.murillons.login_auth.enums.Role;

public class UserResponse {
    private String email;
    private Role role;

    public UserResponse() {
    }

    public UserResponse(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}