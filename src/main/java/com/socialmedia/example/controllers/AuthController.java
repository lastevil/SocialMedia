package com.socialmedia.example.controllers;

import com.socialmedia.example.dto.UserRegDto;
import com.socialmedia.example.exception.AppAuthenticationException;
import com.socialmedia.example.models.JwtRequest;
import com.socialmedia.example.models.JwtResponse;
import com.socialmedia.example.services.UserService;
import com.socialmedia.example.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    @Operation(summary = "authorization request")
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
    @Operation(summary = "registration request")
    public void regestration(@RequestBody UserRegDto userRegDto) {
        userService.tryNewUserAdd(userRegDto);
    }
}
