package com.socialmedia.example.controllers;

import com.socialmedia.example.dto.requests.RequestMessageDto;
import com.socialmedia.example.dto.responses.ResponseDtoMessage;
import com.socialmedia.example.exception.validators.TokenUserValidator;
import com.socialmedia.example.services.interfaces.MessengerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Messages", description = "Контроллер работы с сообщениями \"друзей\"")
public class MessageController {
    private final MessengerServiceImpl messengerService;

    @PostMapping("/message/send")
    @Operation(summary = "Отправка сообщения другу", description = "Отправка сообщения другу")
    public void sendMessage(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                            @RequestBody RequestMessageDto messageDto) {
        String username = TokenUserValidator.validate(token);
        messengerService.sendMessage(username, messageDto);
    }

    @PutMapping("/message/prove")
    @Operation(summary = "Исправление отправленного сообщения", description = "Исправление отправленного сообщения")
    public void proveMessageText(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                                 @RequestBody RequestMessageDto messageDto) {
        String username = TokenUserValidator.validate(token);
        messengerService.proveMessageText(username, messageDto);
    }

    @GetMapping("/message/user/{userId}")
    @Operation(summary = "Получить переписку с другом", description = "Метод получения сообщений с другом")
    public List<ResponseDtoMessage> getMessagesFromFriend(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                                                          @PathVariable UUID userId) {
        String username = TokenUserValidator.validate(token);
        return messengerService.getMessages(username, userId);
    }

    @GetMapping("/message/user")
    @Operation(summary = "Получить сообщения от себя", description = "Получить сообщения от себя")
    public List<ResponseDtoMessage> getSelfMessages(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token) {
        String username = TokenUserValidator.validate(token);
        return messengerService.getSelfMessages(username);
    }
}
