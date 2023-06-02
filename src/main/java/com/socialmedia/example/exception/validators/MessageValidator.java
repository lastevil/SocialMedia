package com.socialmedia.example.exception.validators;

import com.socialmedia.example.dto.requests.RequestMessageDto;
import com.socialmedia.example.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class MessageValidator {
    private MessageValidator() {
    }

    public static void validate(RequestMessageDto messageDto) {
        List<String> errors = new ArrayList<>();

        if (messageDto == null) {
            errors.add("Missing message");
        }
        if (messageDto.getMessage()==null || messageDto.getMessage().isBlank()){
            errors.add("Message is empty");
        }
        if(messageDto.getReceiver()==null && messageDto.getId()==null){
            errors.add("Wrong message data");
        }
        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
