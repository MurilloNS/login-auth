package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.configuration.JwtTokenProvider;
import com.murillons.login_auth.dto.LoginRequestDto;
import com.murillons.login_auth.dto.LoginResponseDto;
import com.murillons.login_auth.dto.UserResponseDto;
import com.murillons.login_auth.entities.Role;
import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.exceptions.EmailAlreadyExistsException;
import com.murillons.login_auth.exceptions.InvalidCredentialsException;
import com.murillons.login_auth.exceptions.NotFoundException;
import com.murillons.login_auth.repositories.RoleRepository;
import com.murillons.login_auth.repositories.UserRepository;
import com.murillons.login_auth.services.EmailService;
import com.murillons.login_auth.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    @Override
    public UserResponseDto registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email já registrado!");
        }

        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            Role defaultRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new NotFoundException("Role padrão não encontrada."));
            roles = Collections.singleton(defaultRole);
        } else {
            roles = roles.stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new NotFoundException("Role não encontrada: " + role.getName()))).collect(Collectors.toSet());
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(savedUser.getName());
        userResponseDto.setEmail(savedUser.getEmail());
        userResponseDto.setRoles(savedUser.getRoles());

        return userResponseDto;
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails);
            return new LoginResponseDto(token);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Email ou senha inválidos!");
        }
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> new UserResponseDto(user.getName(), user.getEmail(), user.getRoles())).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
        return new UserResponseDto(user.getName(), user.getEmail(), user.getRoles());
    }
}