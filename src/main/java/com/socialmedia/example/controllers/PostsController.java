package com.socialmedia.example.controllers;

import com.socialmedia.example.dto.requests.RequestPostDto;
import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.exception.validators.TokenUserValidator;
import com.socialmedia.example.services.interfaces.PostServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Posts", description = "Контроллер работы с постами")
public class PostsController {
    private final PostServiceImpl postsService;

    @Operation(summary = "Создание поста пользователем", description = "Создание поста пользователем")
    @PostMapping("/post/add")
    public UUID createPost(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                           @RequestBody RequestPostDto requestPostDto) {
        String username = TokenUserValidator.validate(token);
        return postsService.createPost(username, requestPostDto);
    }

    @Operation(summary = "Добавление/обновление картинки к посту пользователя", description = "Добавление/обновление картинки к посту пользователя")
    @PostMapping("/post/{postId}/file")
    public void addFileToPost(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                              @PathVariable(value = "postId") UUID postId,
                              @RequestPart(value = "data") MultipartFile file) {
        String username = TokenUserValidator.validate(token);
        postsService.addOrUpdateFile(username, postId, file);
    }


    @GetMapping("/post/{postId}")
    @Operation(summary = "Просмотр поста", description = "Получить пост по его id")
    public ResponsePostDto getPostById(@PathVariable UUID postId) {
        return postsService.getPostById(postId);
    }

    @GetMapping(value = "/post/{postId}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Получить картинку поста", description = "Получить картинку поста по его id")
    public HttpEntity<byte[]> getPostFileById(@PathVariable UUID postId) {
        return postsService.getPostFile(postId);
    }

    @PutMapping("/post/{postId}")
    @Operation(summary = "Обновление поста пользователя", description = "Обновить пост пользователя по id")
    public void updatePost(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                           @PathVariable UUID postId,
                           @RequestBody RequestPostDto requestPostDto) {
        String username = TokenUserValidator.validate(token);
        postsService.updatePost(username, postId, requestPostDto);
    }

    @DeleteMapping("/post/{postId}")
    @Operation(summary = "Удаление поста пользователя", description = "Удалить пост пользователя по id")
    public void deletePost(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                           @PathVariable UUID postId) {
        String username = TokenUserValidator.validate(token);
        postsService.deletePost(username, postId);
    }

    @DeleteMapping("/post/{postId}/file")
    @Operation(summary = "Удаление картинки из поста", description = "Удалить картинку из поста по его id")
    public void deletePostFile(@HeadersSecurityMarker UsernamePasswordAuthenticationToken token,
                               @PathVariable UUID postId) {
        String username = TokenUserValidator.validate(token);
        postsService.deleteFileFromPost(username, postId);
    }
}
