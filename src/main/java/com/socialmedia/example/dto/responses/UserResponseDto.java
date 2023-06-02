package com.socialmedia.example.dto.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class UserResponseDto {
    UUID id;
    String username;
    String email;
}
