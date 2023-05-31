package com.socialmedia.example.exception;

public class ResourceExistsException extends RuntimeException{
    public ResourceExistsException(String s){
        super(s);
    }
}
