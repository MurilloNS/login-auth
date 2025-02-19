package com.murillons.login_auth.services.impl;

import com.murillons.login_auth.services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = "http://localhost:8080/api/user/reset-password?token=" + token;
        String subject = "Recuperação de Senha";
        String message = "Olá,\n\nClique no link abaixo para redefinir sua senha:\n\n" + resetUrl;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("murillo.murillo2001@gmail.com");
        mailSender.send(mailMessage);
    }
}