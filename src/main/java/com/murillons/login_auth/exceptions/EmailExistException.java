package com.murillons.login_auth.exceptions;

public class EmailExistException extends RuntimeException {
    public EmailExistException(String message) {
        super(message);
    }
}