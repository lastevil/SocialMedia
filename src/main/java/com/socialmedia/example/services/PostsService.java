package com.socialmedia.example.services;

import com.socialmedia.example.converters.PostMapper;
import com.socialmedia.example.dto.RequestPostDto;
import com.socialmedia.example.dto.ResponsePostDto;
import com.socialmedia.example.entities.Post;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.exception.AccessDeniedException;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.validators.PostValidator;
import com.socialmedia.example.repositorys.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsService {
    private final UserService userService;
    private final PostRepository postRepository;

    @Transactional
    public UUID createPost(String username, RequestPostDto requestPostDto) {
        PostValidator.requestDtoValidate(requestPostDto);
        Post post = Post.builder()
                .header(requestPostDto.getHeader())
                .body(requestPostDto.getBody())
                .user(userService.findUserByUsername(username))
                .build();
        postRepository.save(post);
        return post.getId();
    }

    public List<Post> getPostsByUser(UUID userId) {
        return postRepository.findAllByUserId(userId);
    }

    public ResponsePostDto getPostById(UUID postId) {
        return PostMapper.INSTANCE.fromEntityToResp(postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not exist")));
    }

    @Transactional
    public void updatePost(String username, UUID postId, RequestPostDto requestPostDto) {
        User user = userService.findUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Access Denied");
        }
        if (!requestPostDto.getHeader().isBlank()) {
            post.setHeader(requestPostDto.getHeader());
        }
        if (!requestPostDto.getBody().isBlank()) {
            post.setBody(requestPostDto.getBody());
        }
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(String username, UUID postId) {
        User user = userService.findUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Access Denied");
        }
        if (post.getPhotoLink() != null) {
            try {
                Files.deleteIfExists(Paths.get(post.getPhotoLink()));
            } catch (IOException e) {
                throw new ResourceNotFoundException("File not found");
            }
        }
        postRepository.deleteById(postId);
    }

    private Path createDirectory(String username, UUID postid) {
        Path root = Paths.get("images");
        try {
            if (!Files.exists(root)) {

                Files.createDirectories(root);
            }
            root = Paths.get(root.toString(), username);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            root = Paths.get(root.toString(), postid.toString());
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new AccessDeniedException("Could not initialize folder for upload!");
        }
        return root;
    }

    @Transactional
    public void addOrUpdateFile(String username, UUID postId, MultipartFile file) {
        if (file != null) {
            User user = userService.findUserByUsername(username);
            String contentType = file.getContentType();
            if (!contentType.contains("image/")) {
                throw new AccessDeniedException("Invalid file type");
            }
            Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not exist"));
            if (!post.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied");
            }
            Path path = createDirectory(username, postId);
            path = Paths.get(path.toString(), file.getOriginalFilename());
            try {
                if (Files.exists(path)) {
                    Files.delete(path);
                }
                Files.write(path, file.getBytes());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            post.setPhotoLink(path.toString());
            post.setPhotoType(file.getContentType());
            postRepository.save(post);
        }
    }

    @Transactional
    public void deleteFileFromPost(String username, UUID postId) {
        User user = userService.findUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Access Denied");
        }
        if (post.getPhotoLink() != null) {
            try {
                Files.deleteIfExists(Paths.get(post.getPhotoLink()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            post.setPhotoLink(null);
            post.setPhotoType(null);
            postRepository.save(post);
        }
    }

    public HttpEntity<byte[]> getPostFile(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        byte[] image;
        long size;
        if (post.getPhotoLink() != null) {
            String link = post.getPhotoLink();
            try {
                image = Files.readAllBytes(Paths.get(link));
                size = Files.size(Paths.get(link));
            } catch (IOException e) {
                throw new ResourceNotFoundException("File cant readable");
            }
        } else {
            throw new ResourceNotFoundException("File not found");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(post.getPhotoType()));
        headers.setContentLength(size);
        return new HttpEntity<>(image, headers);
    }
}
