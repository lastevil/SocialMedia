package com.socialmedia.example.converters;

import com.socialmedia.example.dto.requests.UserRegDto;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Mapping(expression = "java(regDto.getUsername().toLowerCase())", target = "username")
    @Mapping(expression = "java(regDto.getEmail().toLowerCase())", target = "email")
    @Mapping(expression = "java(passwordEncoder.encode(regDto.getPassword()))", target = "password")
    public abstract User fromRegDto(UserRegDto regDto);

    public abstract UserResponseDto fromEntityToRespDto(User user);

}
