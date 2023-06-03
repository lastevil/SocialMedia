package com.socialmedia.example.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRegDto {

    @Schema(title = "Имя пользователя", description = "Имя пользователя", example = "test", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(title = "Электронная почта", description = "Электронная почта пользователя", example = "test@test.ru", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(title = "Пароль", description = "Пароль пользователя", example = "Qwerty123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}