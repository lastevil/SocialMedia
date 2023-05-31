package com.socialmedia.example.converters;

import com.socialmedia.example.dto.UserRegDto;
import com.socialmedia.example.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public User fromRegDto(UserRegDto regDto){
        User user = new User();
        user.setUsername(regDto.getUsername());
        user.setEmail(regDto.getEmail());
        user.setPassword(passwordEncoder.encode(regDto.getPassword()));
        return user;
    }
}
