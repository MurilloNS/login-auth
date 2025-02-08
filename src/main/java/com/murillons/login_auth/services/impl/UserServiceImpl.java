package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.dto.UserRequest;
import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.enums.Role;
import com.murillons.login_auth.exceptions.EmailExistException;
import com.murillons.login_auth.repositories.UserRepository;
import com.murillons.login_auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailExistException("Este e-mail já está cadastrado!");
        }

        boolean isFirstUser = userRepository.count() == 0;
        Role userRole = userRequest.getRole() != null ? userRequest.getRole() : (isFirstUser ? Role.ADMIN : Role.USER);
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRole);
        return userRepository.save(user);
    }
}