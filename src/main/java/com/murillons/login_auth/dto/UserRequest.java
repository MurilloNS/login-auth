package com.murillons.login_auth.dto;

import com.murillons.login_auth.enums.Role;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {
    @NotBlank(message = "O e-mail é obrigatório!")
    private String email;

    @NotBlank(message = "A senha é obrigatória!")
    private String password;
    private Role role;

    public UserRequest(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}