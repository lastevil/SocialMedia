package com.socialmedia.example.exception.validators;

import com.socialmedia.example.dto.requests.UserRegDto;
import com.socialmedia.example.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {

    private final Pattern pattern;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    UserValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean emailValidate(final String hex) {
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public void validate(UserRegDto userRegDto) {

        List<String> errors = new ArrayList<>();
        if (userRegDto.getUsername() == null || userRegDto.getUsername().isBlank())
            errors.add("Username must be not empty");
        if (userRegDto.getPassword() == null || userRegDto.getPassword().isBlank())
            errors.add("Password must not be empty");
        if (userRegDto.getEmail() == null || userRegDto.getEmail().isBlank())
            errors.add("Email must not be empty");
        if (!emailValidate(userRegDto.getEmail()))
            errors.add("Email is not valid");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}
