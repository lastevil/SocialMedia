package com.socialmedia.example.exception.validators;

import com.socialmedia.example.dto.requests.RequestPostDto;
import com.socialmedia.example.exception.AccessDeniedException;
import com.socialmedia.example.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostValidator {

    private PostValidator(){}

    public static void requestDtoValidate(RequestPostDto requestPostDto){
        List<String> errors = new ArrayList<>();
        if (requestPostDto.getHeader() == null || requestPostDto.getHeader().isBlank())
            errors.add("Header must be not empty");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
    public static void accessValidate(UUID id1, UUID id2){
        if(!id1.equals(id2)){
            throw new AccessDeniedException("Access Denied");
        }
    }
}
