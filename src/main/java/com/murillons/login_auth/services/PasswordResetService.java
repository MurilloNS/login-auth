package com.murillons.login_auth.services;

import com.murillons.login_auth.dto.PasswordResetRequestDto;
import com.murillons.login_auth.entities.User;

public interface PasswordResetService {
    void sendPasswordResetEmail(String email);
    String generatePasswordResetToken(User user);
    void sendEmail(String email, String token);
    void resetPassword(String token, String newPassword);
}