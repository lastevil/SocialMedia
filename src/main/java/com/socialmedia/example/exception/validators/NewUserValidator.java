package com.socialmedia.example.exception.validators;

import com.socialmedia.example.dto.UserRegDto;
import com.socialmedia.example.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class NewUserValidator {
    public  static void validate(UserRegDto userRegDto) {

        List<String> errors = new ArrayList<>();
        if (userRegDto.getUsername() == null || userRegDto.getUsername().isBlank())
            errors.add("Username must be not empty");
        if (userRegDto.getPassword() == null || userRegDto.getPassword().isBlank())
            errors.add("Password must not be empty");
        if (userRegDto.getEmail() == null || userRegDto.getEmail().isBlank())
            errors.add("Email must not be empty");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}
