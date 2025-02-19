package com.murillons.login_auth.services;

import com.murillons.login_auth.dto.LoginRequestDto;
import com.murillons.login_auth.dto.LoginResponseDto;
import com.murillons.login_auth.dto.UserResponseDto;
import com.murillons.login_auth.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(User user);
    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserByEmail(String email);
}