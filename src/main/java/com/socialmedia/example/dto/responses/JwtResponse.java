package com.socialmedia.example.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    @Schema(title = "Токен",description = "JWT токен")
    private String token;
}
