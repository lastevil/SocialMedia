package com.socialmedia.example.converters;

import com.socialmedia.example.dto.requests.UserRegDto;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.entities.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(expression = "java(regDto.getUsername().toLowerCase())", target = "username")
    @Mapping(expression = "java(regDto.getEmail().toLowerCase())", target = "email")
    @Mapping(expression = "java(encoder.encode(regDto.getPassword()))", target = "password")
    User fromRegDto(UserRegDto regDto, @Context PasswordEncoder encoder);

    UserResponseDto fromEntityToRespDto(User user);

}
