package com.socialmedia.example.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;
@Data
public class ResponsePostDto {
    @Schema(description = "id поста", example = "1")
    private UUID id;
    @Schema(description = "Заголовок поста", example = "Выезд на море")
    private String header;
    @Schema(description = "Содержание поста", example = "Была спокойная погода")
    private String body;
    @Schema(description = "Наличие фотографии", example = "true")
    private boolean photo;
    @Schema(description = "Дата создания", example = "2023-06-03T00:49:04.077786+05:00")
    private OffsetDateTime createdAt;
}
