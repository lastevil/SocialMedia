package com.socialmedia.example;

import com.socialmedia.example.converters.PostMapper;
import com.socialmedia.example.dto.requests.RequestPostDto;
import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.entities.Post;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.ValidationException;
import com.socialmedia.example.repositorys.PostRepository;
import com.socialmedia.example.services.PostsService;
import com.socialmedia.example.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {PostsService.class})
class PostsServiceTest {

    @Autowired
    private PostsService postsService;
    @MockBean
    private UserService userService;
    @MockBean
    private PostRepository postRepository;

    @Test
    void createPost() {
        UUID id1 = UUID.randomUUID();
        User user1 = new User(id1, "1", "u1.email.ru", null, null, LocalDateTime.now(), null);

        RequestPostDto dto1 = new RequestPostDto();
        dto1.setHeader("TestHeader1");
        dto1.setBody("TestBody1");
        Post post = Post.builder()
                .header(dto1.getHeader())
                .id(id1)
                .body(dto1.getBody())
                .user(user1)
                .build();

        Mockito.doReturn(user1).when(userService).findUserByUsername("1");
        Mockito.doReturn(post).when(postRepository).save(any(Post.class));
        UUID result = postsService.createPost("1", dto1);

        Assertions.assertEquals(id1, result);

        RequestPostDto dto2 = new RequestPostDto();
        dto1.setHeader("");
        dto1.setBody("TestBody1");
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> postsService.createPost("1", dto2));
        Assertions.assertEquals("Header must be not empty", exception.getMessage());

        exception = Assertions.assertThrows(ValidationException.class, () -> postsService.createPost("1", null));
        Assertions.assertEquals("Wrong request data", exception.getMessage());
    }


    @Test
    void getPostById() {
        UUID id1 = UUID.randomUUID();
        RequestPostDto dto1 = new RequestPostDto();
        dto1.setHeader("TestHeader1");
        dto1.setBody("TestBody1");
        User user1 = new User(id1, "1", "u1.email.ru", null, null, LocalDateTime.now(), null);

        Optional<Post> post1 = Optional.of(Post.builder()
                .header(dto1.getHeader())
                .id(id1)
                .body(dto1.getBody())
                .user(user1)
                .build());

        Mockito.doReturn(post1).when(postRepository).findById(id1);

        ResponsePostDto result = postsService.getPostById(id1);
        ResponsePostDto expect = PostMapper.INSTANCE.fromEntityToResp(post1.get());
        Assertions.assertEquals(expect, result);

        Optional<Post> post2 = Optional.empty();
        Mockito.doReturn(post2).when(postRepository).findById(any(UUID.class));
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> postsService.getPostById(id1));
        Assertions.assertEquals("Post not exist", exception.getMessage());
    }
}