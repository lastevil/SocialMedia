package com.socialmedia.example.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RequestMessageDto {
    @Schema(title = "id сообщения", description = "заполняется при изменении сообщения", example = "1", requiredMode = Schema.RequiredMode.AUTO)
    private Long id;
    @Schema(title = "Содержание сообщения", description = "не может быть пустым", example = "Текст", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    @Schema(title = "id получателя", description = "заполняется при отправке сообщения пользователю", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", requiredMode = Schema.RequiredMode.AUTO)
    private UUID receiver;
}
