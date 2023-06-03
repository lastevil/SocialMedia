package com.socialmedia.example.services.interfaces;

import com.socialmedia.example.dto.requests.RequestPostDto;
import com.socialmedia.example.dto.responses.ResponsePostDto;
import org.springframework.http.HttpEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PostServiceImpl {
    UUID createPost(String username, RequestPostDto requestPostDto);
    ResponsePostDto getPostById(UUID postId);
    void updatePost(String username, UUID postId, RequestPostDto requestPostDto);
    void deletePost(String username, UUID postId);
    void addOrUpdateFile(String username, UUID postId, MultipartFile file);
    void deleteFileFromPost(String username, UUID postId);
    HttpEntity<byte[]> getPostFile(UUID postId);
}

