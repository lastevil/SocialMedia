package com.socialmedia.example.services.interfaces;

import com.socialmedia.example.dto.responses.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface SubscribeServiceImpl {
    void subscribe(String username, UUID userId);

    void approveFriend(String username, UUID userId);

    void deleteFromFriend(String username, UUID userId);

    void unsubscribe(String username, UUID userId);


    List<UserResponseDto> getFriendsList(String username);
}
