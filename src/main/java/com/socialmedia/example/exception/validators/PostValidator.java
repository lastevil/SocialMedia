package com.socialmedia.example.exception.validators;

import com.socialmedia.example.dto.requests.RequestPostDto;
import com.socialmedia.example.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class PostValidator {

    private PostValidator(){}

    public static void requestDtoValidate(RequestPostDto requestPostDto){
        List<String> errors = new ArrayList<>();
        if (requestPostDto.getHeader() == null || requestPostDto.getHeader().isBlank())
            errors.add("Header must be not empty");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }

}
