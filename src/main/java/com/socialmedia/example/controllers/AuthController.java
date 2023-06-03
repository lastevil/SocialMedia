package com.socialmedia.example.controllers;

import com.socialmedia.example.dto.requests.UserRegDto;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.exception.AppAuthenticationException;
import com.socialmedia.example.dto.requests.JwtRequest;
import com.socialmedia.example.dto.responses.JwtResponse;
import com.socialmedia.example.services.UserService;
import com.socialmedia.example.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Auth", description = "Контроллер авторизации и регистрации")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    @Operation(summary = "Запрос авторизации",description = "Вход по имени пользователя или email")
    public JwtResponse login(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AppAuthenticationException("Incorrect user data");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getLogin());
        String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @PostMapping("/registration")
    @Operation(summary = "Запрос регистрации", description = "Отправка регистрационных данных")
    public void regestration(@RequestBody UserRegDto userRegDto) {
        userService.tryNewUserAdd(userRegDto);
    }

    @GetMapping("/users")
    @Operation(summary = "Получить список пользователей", description = "Получить список всех пользователей")
    public List<UserResponseDto> getUsers() {
        return userService.getAllUsers();
    }
}
