package com.murillons.login_auth.services;

import com.murillons.login_auth.dto.UserRequest;
import com.murillons.login_auth.entities.User;

public interface UserService {
    public User registerUser(UserRequest user);
}