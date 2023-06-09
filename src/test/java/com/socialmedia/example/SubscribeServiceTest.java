package com.socialmedia.example;

import com.socialmedia.example.configs.AppConfig;
import com.socialmedia.example.converters.UserMapper;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.entities.Subscriber;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.repositorys.SubscribeRepository;
import com.socialmedia.example.services.MessengerService;
import com.socialmedia.example.services.SubscribeService;
import com.socialmedia.example.services.UserService;
import com.socialmedia.example.services.interfaces.SubscribeServiceImpl;
import com.socialmedia.example.services.interfaces.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest(classes = {SubscribeService.class, UserMapper.class, AppConfig.class})
class SubscribeServiceTest {

    @Autowired
    private SubscribeServiceImpl service;

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private SubscribeRepository subscribeRepository;
    @MockBean
    private MessengerService messengerService;

    @Test
    void getFriendsListTest(){
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        User user1 = new User(id1, "1", "u1.email.ru", null, null, LocalDateTime.now(), null);
        User user2 = new User(id2, "2", "u2.email.ru", null, null, LocalDateTime.now(), null);
        User user3 = new User(id3, "3", "u3.email.ru", null, null, LocalDateTime.now(), null);
        Mockito.doReturn(user1).when(userService).findUserByUsername("1");
        List<Subscriber> userList = new ArrayList<>();
        Subscriber subscriber1 = new Subscriber();
        subscriber1.setFriends(true);
        subscriber1.setSender(user1);
        subscriber1.setReceiver(user2);
        subscriber1.setId(1L);
        Subscriber subscriber2 = new Subscriber();
        subscriber2.setSender(user1);
        subscriber2.setReceiver(user3);
        subscriber2.setFriends(true);
        subscriber2.setId(2L);
        userList.add(subscriber1);
        userList.add(subscriber2);
        Mockito.doReturn(userList).when(subscribeRepository).findBySender_IdAndIsFriends(id1, true);

        List<UserResponseDto> responseList = service.getFriendsList("1");
        List<User> users = new ArrayList<>();
        users.add(user2);
        users.add(user3);
        List<UserResponseDto> expectedList = users.stream()
                .map(UserMapper.INSTANCE::fromEntityToRespDto)
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedList,responseList);
    }

}
