package com.socialmedia.example.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestPostDto {
    @Schema(title = "Заголовок поста", description = "Заголовок поста", example = "Поездка в горы", requiredMode = Schema.RequiredMode.REQUIRED)
    private String header;
    @Schema(title = "Тело поста", description = "Описание поста", example = "Подъем был тяжелым", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String body;
}
