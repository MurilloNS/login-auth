package com.murillons.login_auth.controllers;

import com.murillons.login_auth.configuration.JwtUtil;
import com.murillons.login_auth.documentation.UserApi;
import com.murillons.login_auth.dto.AuthResponse;
import com.murillons.login_auth.dto.UserRequest;
import com.murillons.login_auth.dto.UserResponse;
import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.exceptions.AuthenticationException;
import com.murillons.login_auth.exceptions.UserNotFoundException;
import com.murillons.login_auth.services.UserService;
import com.murillons.login_auth.services.impl.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Validated
@CrossOrigin("http://localhost:3000")
public class UserController implements UserApi {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        User registeredUser = userService.registerUser(userRequest);
        UserResponse userResponse = new UserResponse(registeredUser.getEmail(), registeredUser.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("Usuário não encontrado.");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Senha inválida.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        passwordResetService.sendPasswordResetEmail(email);
        return ResponseEntity.ok("E-mail de recuperação enviado.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String token, @RequestParam String newPassword) {
        passwordResetService.resetPassword(email, token, newPassword);
        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }
}