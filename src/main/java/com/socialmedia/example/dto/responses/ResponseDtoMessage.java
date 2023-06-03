package com.socialmedia.example.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ResponseDtoMessage {
    @Schema(description = "Текст сообщения", example = "test message")
    private String messageText;
    @Schema(description = "От пользователя (Имя пользователя)", example = "testuser1")
    private String fromUser;
    @Schema(description = "К пользователю (Имя пользователя)", example = "testuser2")
    private String toUser;
    @Schema(description = "Дата создания", example = "2023-06-03T00:49:04.077786+05:00")
    private OffsetDateTime createdAt;
}
