package com.socialmedia.example.controllers;

import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.exception.validators.TokenUserValidator;
import com.socialmedia.example.services.interfaces.FeedServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Feed", description = "Контроллер работы с лентой активности")
public class FeedController {
    private final FeedServiceImpl feedService;

    @GetMapping("/feed/{page}/{size}/{timeUp}")
    @Operation(summary = "Получение постов из подписок", description = "Получение постов из подписок")
    public Page<ResponsePostDto> getSubscribePost(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                                                  @PathVariable int page,
                                                  @PathVariable int size,
                                                  @PathVariable boolean timeUp) {
        String username = TokenUserValidator.validate(token);
        return feedService.getFeed(username, page, size, timeUp);
    }
    @GetMapping("/post/user/{userId}")
    @Operation(summary = "Список постов пользователя", description = "Получить список постов пользователя по его id")
    public List<ResponsePostDto> getUserPost(@PathVariable UUID userId) {
        return feedService.getPostsByUser(userId);
    }

    @GetMapping("/post")
    @Operation(summary = "Получить посты текущего пользователя", description = "Метод получения постов для текущего пользователя")
    public List<ResponsePostDto> getPostCurrentUser(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token) {
        String username = TokenUserValidator.validate(token);
        return feedService.getCurrentUserPost(username);
    }
}
