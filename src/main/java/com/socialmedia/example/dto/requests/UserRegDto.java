package com.socialmedia.example.dto.requests;

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