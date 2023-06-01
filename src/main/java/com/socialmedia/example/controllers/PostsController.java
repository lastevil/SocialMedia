package com.socialmedia.example.controllers;

import com.socialmedia.example.entities.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Posts", description = "Контроллера работы с постами")
public class PostsController {

    @Operation(summary = "Создание поста пользователем", description = "Создание поста пользователем")
    @PostMapping("/post/add")
    public void createPost() {
    }

    @GetMapping("/post/{userId}")
    @Operation(summary = "Список постов пользователя", description = "Получить список постов пользователя по его id")
    public List<Post> getUserPost(@PathVariable UUID userId) {
        return null;
    }


}
