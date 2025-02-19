package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.configuration.JwtTokenProvider;
import com.murillons.login_auth.dto.PasswordResetRequestDto;
import com.murillons.login_auth.entities.User;
import com.murillons.login_auth.exceptions.NotFoundException;
import com.murillons.login_auth.repositories.UserRepository;
import com.murillons.login_auth.services.PasswordResetService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.jwt.password-reset-expiration}")
    private long resetTokenExpiration;

    public PasswordResetServiceImpl(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("E-mail não encontrado!"));

        String token = generatePasswordResetToken(user);
        sendEmail(user.getEmail(), token);
    }

    @Override
    public String generatePasswordResetToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + resetTokenExpiration))
                .signWith(jwtTokenProvider.key(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public void sendEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Solicitação de Recuperação de Senha");
        message.setText("Clique no link para redefinir sua senha: http://localhost:8080/api/password-reset/confirm?token=" + token);
        mailSender.send(message);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Token inválido ou expirado");
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}