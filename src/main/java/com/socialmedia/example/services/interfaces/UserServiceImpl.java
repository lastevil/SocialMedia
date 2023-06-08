package com.socialmedia.example.services.interfaces;

import com.socialmedia.example.dto.requests.UserRegDto;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserServiceImpl extends UserDetailsService {
    List<UserResponseDto> getAllUsers();
    User findUserByUserId(UUID userId);

    User findUserByUsername(String username);
    void tryNewUserAdd(UserRegDto newUser);
}
