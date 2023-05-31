package com.socialmedia.example.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationError {
    private int statusCode;
    private String message;
}
