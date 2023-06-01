package com.socialmedia.example.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class ResponsePostDto {
    private UUID id;
    private String header;
    private String body;
    private boolean photo;
}
