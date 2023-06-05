package com.socialmedia.example.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class UserResponseDto {
    @Schema(description = "id пользователя", example = "1")
    UUID id;
    @Schema(description = "имя пользователя" , example = "user1")
    String username;
    @Schema(description = "email пользователя", example = "test@test.ru")
    String email;
}
