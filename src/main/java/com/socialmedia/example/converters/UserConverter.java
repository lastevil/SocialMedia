package com.socialmedia.example.converters;

import com.socialmedia.example.dto.requests.UserRegDto;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public User fromRegDto(UserRegDto regDto) {
        User user = new User();
        user.setUsername(regDto.getUsername().toLowerCase());
        user.setEmail(regDto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(regDto.getPassword()));
        return user;
    }

    public UserResponseDto fromEntityToRespDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
