package com.socialmedia.example.exception.validators;

import com.socialmedia.example.exception.ValidationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class TokenUserValidator {

    private TokenUserValidator() {
    }

    public static String validate(UsernamePasswordAuthenticationToken token) {
        List<String> errors = new ArrayList<>();
        org.springframework.security.core.userdetails.User user;
        if (token == null) {
            errors.add("Missing token");
            throw new ValidationException(errors);
        } else {
            user = (User) token.getPrincipal();
        }
        return user.getUsername();
    }
}
