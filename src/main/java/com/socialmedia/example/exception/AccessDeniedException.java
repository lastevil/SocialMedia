package com.socialmedia.example.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String s){
        super(s);
    }
}
