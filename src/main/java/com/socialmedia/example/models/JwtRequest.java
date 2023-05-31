package com.socialmedia.example.models;

import lombok.Data;

@Data
public class JwtRequest {
    private String login;
    private String password;
}
