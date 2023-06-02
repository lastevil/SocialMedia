package com.socialmedia.example.dto.requests;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RequestMessageDto {
    private Long id;
    private String message;
    private UUID receiver;
}
