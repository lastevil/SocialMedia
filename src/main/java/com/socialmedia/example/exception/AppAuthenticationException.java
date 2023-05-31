package com.socialmedia.example.exception;

public class AppAuthenticationException extends RuntimeException{
    public AppAuthenticationException(String message) {
        super(message);
    }
}
