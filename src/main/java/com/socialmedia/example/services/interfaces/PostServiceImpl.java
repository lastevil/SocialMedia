package com.socialmedia.example.services.interfaces;

import com.socialmedia.example.dto.requests.RequestPostDto;
import com.socialmedia.example.dto.responses.ResponsePostDto;
import org.springframework.http.HttpEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostServiceImpl {
    UUID createPost(String username, RequestPostDto requestPostDto);
    List<ResponsePostDto> getPostsByUser(UUID userId);
    ResponsePostDto getPostById(UUID postId);
    void updatePost(String username, UUID postId, RequestPostDto requestPostDto);
    void deletePost(String username, UUID postId);
    void addOrUpdateFile(String username, UUID postId, MultipartFile file);
    void deleteFileFromPost(String username, UUID postId);
    HttpEntity<byte[]> getPostFile(UUID postId);
    List<ResponsePostDto> getCurrentUserPost(String username);
}

