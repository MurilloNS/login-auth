package com.murillons.login_auth.controllers;

import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.exceptions.EmailExistException;
import com.murillons.login_auth.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validUser = new User();
        validUser.setId(1L);
        validUser.setEmail("murillo@example.com");
        validUser.setPassword("password");
    }

    @Test
    void registerUser_Success(){
        when(userService.registerUser(validUser)).thenReturn(validUser);

        ResponseEntity<?> response = userController.registerUser(validUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(validUser, response.getBody());
        verify(userService, times(1)).registerUser(validUser);
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        when(userService.registerUser(validUser)).thenThrow(new EmailExistException("Esse e-mail já está cadastrado!"));

        ResponseEntity<?> response = userController.registerUser(validUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Esse e-mail já está cadastrado!", response.getBody());
        verify(userService, times(1)).registerUser(validUser);
    }
}