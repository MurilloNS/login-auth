package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.exceptions.EmailExistException;
import com.murillons.login_auth.repositories.UserRepository;
import com.murillons.login_auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailExistException("Esse e-mail já está cadastrado!");
        }

        return userRepository.save(user);
    }
}