package com.socialmedia.example.services;

import com.socialmedia.example.converters.MessageMapper;
import com.socialmedia.example.dto.requests.RequestMessageDto;
import com.socialmedia.example.dto.responses.ResponseDtoMessage;
import com.socialmedia.example.entities.Message;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.TimeException;
import com.socialmedia.example.exception.validators.MessageValidator;
import com.socialmedia.example.repositorys.MessageRepository;
import com.socialmedia.example.services.interfaces.MessengerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessengerService implements MessengerServiceImpl {

    private final MessageRepository messageRepository;
    private final UserService userService;

    @Override
    public void sendMessage(String username, RequestMessageDto messageDto) {
        MessageValidator.validate(messageDto);
        if (messageDto.getReceiver() != null) {
            Message message = new Message();
            message.setMessageText(messageDto.getMessage());
            message.setReceiverId(userService.findUserByUserId(messageDto.getReceiver()));
            message.setSenderId(userService.findUserByUsername(username));
            messageRepository.save(message);
        }
    }

    @Override
    @Transactional
    public void proveMessageText(String username, RequestMessageDto messageDto) {
        MessageValidator.validate(messageDto);
        if (messageDto.getId() != null) {
            Message message = messageRepository.findById(messageDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
            if (LocalDateTime.now().minusMinutes(5).isBefore(message.getCreatedAt())) {
                message.setMessageText(messageDto.getMessage());
                messageRepository.save(message);
            } else throw new TimeException("A lot of time has passed");
        }

    }

    @Override
    @Transactional
    public List<ResponseDtoMessage> getMessages(String username, UUID friend) {
        UUID senderId = userService.findUserByUsername(username).getId();
        List<Message> messageList = messageRepository.findAllMessagesBetweenFriends(senderId, friend);
        return messageList.stream()
                .map(MessageMapper.INSTANCE::fromEntity)
                .collect(Collectors.toList());
    }
}
