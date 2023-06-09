package com.socialmedia.example;

import com.socialmedia.example.converters.PostMapper;
import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.entities.Post;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.repositorys.PostRepository;
import com.socialmedia.example.services.FeedService;
import com.socialmedia.example.services.UserService;
import com.socialmedia.example.services.interfaces.FeedServiceImpl;
import com.socialmedia.example.services.interfaces.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest(classes = {FeedService.class})
class FeedServiceTest {
    @Autowired
    private FeedServiceImpl feedService;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private UserServiceImpl userService;

    Post post1, post2, post3;
    UUID id1, id2, id3;
    User user1;

    @BeforeEach
    void initBefore() {

        id1 = UUID.randomUUID();
        id2 = UUID.randomUUID();
        id3 = UUID.randomUUID();

        post1 = new Post();
        post1.setId(id1);
        post1.setHeader("Header1");
        post1.setBody("Body1");
        user1 = new User(id2, "1", "u1.email.ru", null, null, LocalDateTime.now(), null);
        post1.setUser(user1);
        post1.setCreatedAt(LocalDateTime.now());

        post2 = new Post();
        post2.setId(id2);
        post2.setHeader("Header2");
        post2.setBody("Body2");
        User user2 = new User(id3, "2", "u2.email.ru", null, null, LocalDateTime.now(), null);
        post2.setUser(user2);
        post2.setCreatedAt(LocalDateTime.now().plusMinutes(1));

        post3 = new Post();
        post3.setId(id3);
        post3.setHeader("Header3");
        post3.setBody("Body3");
        User user3 = new User(id1, "3", "u3.email.ru", null, null, LocalDateTime.now(), null);
        post3.setUser(user3);
        post3.setCreatedAt(LocalDateTime.now().plusMinutes(1));
    }

    @Test
    void getFeedTest() {
        List<Post> postList = new ArrayList<>();
        postList.add(post1);
        postList.add(post2);
        postList.add(post3);
        Page<Post> posts = new PageImpl<>(postList);
        Mockito.doReturn(user1).when(userService).findUserByUsername("1");
        Mockito.doReturn(posts).when(postRepository).findByUserId(id2, PageRequest.of(0, 4, Sort.by("createdAt")));
        Page<ResponsePostDto> dtoPage = feedService.getFeed("1", 1, 4, true);

        Page<ResponsePostDto> expected = posts.map(PostMapper.INSTANCE::fromEntityToResp);
        Assertions.assertEquals(expected.getTotalElements(), dtoPage.getTotalElements());
    }

    @Test
    void getPostsByUserTest() {
        List<Post> postList = new ArrayList<>();
        postList.add(post1);
        postList.add(post2);
        postList.add(post3);
        Mockito.doReturn(postList).when(postRepository).findAllByUserId(id1);

        List<ResponsePostDto> responseList = feedService.getPostsByUser(id1);
        List<ResponsePostDto> expectedList = postList.stream().map(PostMapper.INSTANCE::fromEntityToResp).collect(Collectors.toList());

        Assertions.assertEquals(responseList, expectedList);
        Assertions.assertEquals(responseList.size(), expectedList.size());

    }
}
