package com.socialmedia.example;

import com.socialmedia.example.converters.MessageMapper;
import com.socialmedia.example.dto.requests.RequestMessageDto;
import com.socialmedia.example.dto.responses.ResponseDtoMessage;
import com.socialmedia.example.entities.Message;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.exception.TimeException;
import com.socialmedia.example.exception.ValidationException;
import com.socialmedia.example.repositorys.MessageRepository;
import com.socialmedia.example.services.MessengerService;
import com.socialmedia.example.services.UserService;
import com.socialmedia.example.services.interfaces.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest(classes = MessengerService.class)
class MessengerServiceTest {

    @Autowired
    private MessengerService service;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private UserServiceImpl userService;

    private UUID id1;
    private UUID id2;

    @BeforeEach
    void initBefore() {
        id1 = UUID.randomUUID();
        id2 = UUID.randomUUID();
    }

    @Test
    void sendMessageTest() {
        RequestMessageDto dto1 = RequestMessageDto.builder()
                .receiver(id1)
                .message("")
                .id(1L)
                .build();

        RequestMessageDto dto2 = RequestMessageDto.builder()
                .message("TEST_MESSAGE")
                .build();

        ValidationException exception1 = Assertions.assertThrows(ValidationException.class,
                () -> service.sendMessage("Username", dto1));
        Assertions.assertEquals("Message is empty", exception1.getMessage());

        exception1 = Assertions.assertThrows(ValidationException.class,
                () -> service.sendMessage("Username", dto2));
        Assertions.assertEquals("Wrong message data", exception1.getMessage());

        exception1 = Assertions.assertThrows(ValidationException.class,
                () -> service.sendMessage("Username", null));
        Assertions.assertEquals("Missing message", exception1.getMessage());
    }

    @Test
    void proveMessageTextTest() {
        Optional<Message> message1 = Optional.of(new Message());
        message1.get().setMessageText("TEST MESSAGE");
        message1.get().setId(1L);
        message1.get().setCreatedAt(LocalDateTime.now().minusDays(1));

        Optional<Message> message2 = Optional.empty();

        RequestMessageDto dto1 = RequestMessageDto.builder()
                .receiver(id1)
                .message("Test")
                .id(1L)
                .build();

        RequestMessageDto dto2 = RequestMessageDto.builder()
                .receiver(id2)
                .message("qwe")
                .id(2L)
                .build();

        Mockito.doReturn(message1).when(messageRepository).findById(1L);
        Mockito.doReturn(message2).when(messageRepository).findById(2L);

        ResourceNotFoundException exception1 = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.proveMessageText("Username", dto2));
        Assertions.assertEquals("Message not found", exception1.getMessage());

        TimeException exception2 = Assertions.assertThrows(TimeException.class,
                () -> service.proveMessageText("Username", dto1));
        Assertions.assertEquals("A lot of time has passed", exception2.getMessage());
    }

    @Test
    void getMessagesTest() {
        Message message1 = new Message();
        message1.setMessageText("TEST MESSAGE");
        message1.setId(1L);
        message1.setSender(new User());
        message1.setReceiver(new User());
        message1.setCreatedAt(LocalDateTime.now().minusMinutes(2));

        Message message2 = new Message();
        message2.setMessageText("TEST MESSAGE");
        message2.setId(2L);
        message2.setReceiver(new User());
        message2.setSender(new User());
        message2.setCreatedAt(LocalDateTime.now().minusMinutes(1));

        User user1 = new User(id1, "Username", "test@test.ru", null, null, LocalDateTime.now(), LocalDateTime.now());
        Mockito.doReturn(user1).when(userService).findUserByUsername("Username");
        List<Message> messageList = new ArrayList<>();
        messageList.add(message1);
        messageList.add(message2);
        Mockito.doReturn(messageList).when(messageRepository).findAllMessagesBetweenFriends(id1, id2);

        List<ResponseDtoMessage> responseDto = service.getMessages("Username", id2);

        List<ResponseDtoMessage> expectedList = messageList.stream().map(MessageMapper.INSTANCE::fromEntity).collect(Collectors.toList());

        Assertions.assertEquals(expectedList, responseDto);
        Assertions.assertEquals(2, responseDto.size());

    }
}