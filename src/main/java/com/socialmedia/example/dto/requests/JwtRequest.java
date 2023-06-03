package com.socialmedia.example.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JwtRequest {
    @Schema(title = "Логин", description = "Имя пользователя или email", example = "test", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @Schema(title = "Пароль", description = "Пароль пользователя", example = "Qwe123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
