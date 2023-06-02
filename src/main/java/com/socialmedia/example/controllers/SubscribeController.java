package com.socialmedia.example.controllers;

import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.exception.validators.TokenUserValidator;
import com.socialmedia.example.services.SubscribeService;
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
@Tag(name = "Subscribe", description = "Контроллер для работы с подписками")
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/friends")
    @Operation(summary = "Список друзей пользователя", description = "Получить список друзей текущего пользователя")
    public List<UserResponseDto> getFriends(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token) {
        String username = TokenUserValidator.validate(token);
        return subscribeService.getFrendsList(username);
    }

    @PostMapping("/subscribe/{userId}")
    @Operation(summary = "Подать заявку на дружбу(Подписаться)", description = "Подать заявку на дружбу пользователя став подпищиком")
    public void subscribe(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token, @PathVariable UUID userId) {
        String username = TokenUserValidator.validate(token);
        subscribeService.subscribe(username, userId);
    }

    @PostMapping("/subscribe/approve/{userId}")
    @Operation(summary = "Подтвердить дружбу", description = "Согласиться на дружбу, взаимная подписка")
    public void approveFriend(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token, @PathVariable UUID userId) {
        String username = TokenUserValidator.validate(token);
        subscribeService.approveFriend(username, userId);
    }

    @DeleteMapping("/subscribe/delete/{friendId}")
    @Operation(summary = "Удалиться из друзей", description = "Удалиться из друзей, отменить подписку")
    public void deleteFromFriend(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                                 @PathVariable UUID friendId) {
        String username = TokenUserValidator.validate(token);
        subscribeService.deleteFromFriend(username, friendId);
    }

    @DeleteMapping("subscribe/{userId}/cancel")
    @Operation(summary = "Отписаться от пользователя",
            description = "Удалить заявку в друзья, отписавшись от пользователя, либо отменить дружбу при еёё наличии")
    public void unsubscribe(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                            @PathVariable UUID userId) {
        String username = TokenUserValidator.validate(token);
        subscribeService.unsubscribe(username, userId);
    }
}
