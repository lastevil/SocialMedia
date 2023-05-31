package com.socialmedia.example.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRegDto {
    private String username;
    private String email;
    private String password;
}