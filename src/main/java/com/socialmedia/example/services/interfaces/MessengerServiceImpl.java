package com.socialmedia.example.services.interfaces;

import com.socialmedia.example.dto.requests.RequestMessageDto;
import com.socialmedia.example.dto.responses.ResponseDtoMessage;

import java.util.List;
import java.util.UUID;

public interface MessengerServiceImpl {

    void sendMessage(String username, RequestMessageDto messageDto);

    void proveMessageText(String username, RequestMessageDto messageDto);

    List<ResponseDtoMessage> getMessages(String username , UUID friend);
}
