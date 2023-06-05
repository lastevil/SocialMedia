package com.socialmedia.example.services;

import com.socialmedia.example.converters.PostMapper;
import com.socialmedia.example.dto.requests.RequestPostDto;
import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.entities.Post;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.exception.AccessDeniedException;
import com.socialmedia.example.exception.FileException;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.validators.PostValidator;
import com.socialmedia.example.repositorys.PostRepository;
import com.socialmedia.example.services.interfaces.PostServiceImpl;
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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsService implements PostServiceImpl {
    private final UserService userService;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public UUID createPost(String username, RequestPostDto requestPostDto) {
        PostValidator.requestDtoValidate(requestPostDto);
        Post post = Post.builder()
                .header(requestPostDto.getHeader())
                .body(requestPostDto.getBody())
                .user(userService.findUserByUsername(username))
                .build();

        return postRepository.save(post).getId();
    }


    @Override
    public ResponsePostDto getPostById(UUID postId) {
        return PostMapper.INSTANCE.fromEntityToResp(postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not exist")));
    }

    @Transactional
    @Override
    public void updatePost(String username, UUID postId, RequestPostDto requestPostDto) {
        User user = userService.findUserByUsername(username);
        Post post = findPostById(postId);
        PostValidator.accessValidate(user.getId(),post.getUser().getId());
        if (!requestPostDto.getHeader().isBlank()) {
            post.setHeader(requestPostDto.getHeader());
        }
        if (!requestPostDto.getBody().isBlank()) {
            post.setBody(requestPostDto.getBody());
        }
        postRepository.save(post);
    }

    @Transactional
    @Override
    public void deletePost(String username, UUID postId) {
        User user = userService.findUserByUsername(username);
        Post post = findPostById(postId);
        PostValidator.accessValidate(user.getId(),post.getUser().getId());
        if (post.getPhotoLink() != null) {
            try {
                Files.deleteIfExists(Paths.get(post.getPhotoLink()));
            } catch (IOException e) {
                throw new ResourceNotFoundException("File not found");
            }
        }
        postRepository.deleteById(postId);
    }

    private Path createDirectory(String username, UUID postId) {
        Path root = Paths.get("images");
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            root = Paths.get(root.toString(), username);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            root = Paths.get(root.toString(), postId.toString());
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new AccessDeniedException("Could not initialize folder for upload!");
        }
        return root;
    }

    @Transactional
    @Override
    public void addOrUpdateFile(String username, UUID postId, MultipartFile file) {
        if (file != null) {
            User user = userService.findUserByUsername(username);
            String contentType = file.getContentType();
            if (contentType!=null && !contentType.contains("image/")) {
                throw new AccessDeniedException("Invalid file type");
            }
            Post post = findPostById(postId);
            PostValidator.accessValidate(user.getId(),post.getUser().getId());
            Path path = createDirectory(username, postId);
            path = Paths.get(path.toString(), file.getOriginalFilename());
            try {
                if (post.getPhotoLink() != null && Files.exists(Paths.get(post.getPhotoLink()))) {
                    Files.delete(Paths.get(post.getPhotoLink()));
                }
                if (Files.exists(path)) {
                    Files.delete(path);
                }
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                throw new FileException("File or directory not found");
            }
            post.setPhotoLink(path.toString());
            post.setPhotoType(file.getContentType());
            postRepository.save(post);
        }
    }

    @Transactional
    @Override
    public void deleteFileFromPost(String username, UUID postId) {
        User user = userService.findUserByUsername(username);
        Post post = findPostById(postId);
        PostValidator.accessValidate(user.getId(),post.getUser().getId());
        if (post.getPhotoLink() != null) {
            try {
                Files.deleteIfExists(Paths.get(post.getPhotoLink()));
            } catch (IOException e) {
                throw new FileException("File or directory not found");
            }
            post.setPhotoLink(null);
            post.setPhotoType(null);
            postRepository.save(post);
        }
    }

    @Override
    public HttpEntity<byte[]> getPostFile(UUID postId) {
        Post post = findPostById(postId);
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


    private Post findPostById(UUID postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }
}
