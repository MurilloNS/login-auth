package com.murillons.login_auth.controllers;

import com.murillons.login_auth.configuration.JwtUtil;
import com.murillons.login_auth.dto.AuthResponse;
import com.murillons.login_auth.dto.UserRequest;
import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.exceptions.EmailExistException;
import com.murillons.login_auth.services.UserService;
import com.murillons.login_auth.services.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    private User validUser;
    private UserRequest validRequest;
    private UserRequest invalidRequest;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validUser = new User();
        validUser.setId(1L);
        validUser.setEmail("murillo@junit.com");
        validUser.setPassword("password");

        validRequest = new UserRequest("murillo@teste.com", "password");
        invalidRequest = new UserRequest("murillo@teste.com", "wrongpassword");

        userDetails = org.springframework.security.core.userdetails.User.withUsername(validRequest.getEmail())
                .password(validRequest.getPassword())
                .roles("USER")
                .build();
    }

    @Test
    void registerUser_Success() {
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

    @Test
    void login_Success() {
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(null);
        when(userDetailsService.loadUserByUsername(validRequest.getEmail())).thenReturn(userDetails);
        when(jwtUtil.generateToken(validRequest.getEmail())).thenReturn("mocked-jwt-token");

        ResponseEntity<?> response = userController.login(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        AuthResponse expectedResponse = new AuthResponse("mocked-jwt-token");
        AuthResponse actualResponse = (AuthResponse) response.getBody();

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getToken(), actualResponse.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userDetailsService, times(1)).loadUserByUsername(validRequest.getEmail());
        verify(jwtUtil, times(1)).generateToken(validRequest.getEmail());
    }

    @Test
    void login_Failure_InvalidCredentials() {
        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<?> response = userController.login(invalidRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuário inválido", response.getBody());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtUtil, never()).generateToken(any());
    }
}