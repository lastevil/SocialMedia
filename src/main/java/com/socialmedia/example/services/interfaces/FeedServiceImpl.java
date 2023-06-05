package com.socialmedia.example.services.interfaces;

import com.socialmedia.example.dto.responses.ResponsePostDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface FeedServiceImpl {
    Page<ResponsePostDto> getFeed(String username, int page, int size, boolean timeUp);

    List<ResponsePostDto> getPostsByUser(UUID userId);

    List<ResponsePostDto> getCurrentUserPost(String username);
}
