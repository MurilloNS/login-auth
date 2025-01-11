package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.exceptions.EmailExistException;
import com.murillons.login_auth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldSaveUser_WhenEmailNotExists() {
        User newUser = new User();
        newUser.setEmail("newuser@example.com");

        when(userRepository.findByEmail("newuser@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User savedUser = userService.registerUser(newUser);

        assertNotNull(savedUser);
        assertEquals("newuser@example.com", savedUser.getEmail());

        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void registerUser_ShouldThrowEmailExistException_WhemEmailExists(){
        User user = new User();
        user.setEmail("existente@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("existente@example.com")).thenReturn(Optional.of(user));

        EmailExistException exception = assertThrows(EmailExistException.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("Esse e-mail já está cadastrado!", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("existente@example.com");
        verify(userRepository, never()).save(user);
    }
}