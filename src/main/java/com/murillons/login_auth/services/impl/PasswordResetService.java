package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.configuration.JwtUtil;
import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordResetService {
    private final Map<String, String> passwordResetTokens = new HashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));

        String resetToken = jwtUtil.generateToken(email);
        passwordResetTokens.put(email, resetToken);
        sendEmail(email, resetToken);
    }

    public boolean validateResetToken(String token, String email) {
        return token.equals(passwordResetTokens.get(email)) && jwtUtil.validateToken(token);
    }

    public void resetPassword(String email, String token, String newPassword) {
        if (!validateResetToken(token, email)) {
            throw new IllegalArgumentException("Token inválido ou expirado.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokens.remove(email);
    }

    private void sendEmail(String email, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperação de Senha");
        message.setText("Clique no link para redefinir sua senha: http://localhost:8080/user/reset-password?token=" + resetToken);
        mailSender.send(message);
    }
}