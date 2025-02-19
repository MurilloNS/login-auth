package com.murillons.login_auth.services;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}