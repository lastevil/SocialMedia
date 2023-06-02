package com.socialmedia.example.dto.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDtoMessage {
    private String messageText;
    private String fromUser;
    private String toUser;
    private LocalDateTime createdAt;
}
