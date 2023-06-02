package com.socialmedia.example.services;

import com.socialmedia.example.converters.UserConverter;
import com.socialmedia.example.dto.requests.RequestMessageDto;
import com.socialmedia.example.dto.responses.UserResponseDto;
import com.socialmedia.example.entities.Subscriber;
import com.socialmedia.example.entities.User;
import com.socialmedia.example.exception.ResourceNotFoundException;
import com.socialmedia.example.repositorys.SubscribeRepository;
import com.socialmedia.example.services.interfaces.MessengerServiceImpl;
import com.socialmedia.example.services.interfaces.SubscribeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscribeService implements SubscribeServiceImpl {
    private final UserService userService;
    private final SubscribeRepository subscribeRepository;
    private final MessengerServiceImpl messengerService;
    private final UserConverter userConverter;

    @Override
    @Transactional
    public void subscribe(String username, UUID userId) {
        User ownUser = userService.findUserByUsername(username);
        User reciverUser = userService.findUserByUserId(userId);
        if (!subscribeRepository.existsBySender_IdAndReceiver_Id(ownUser.getId(), reciverUser.getId())) {
            Subscriber subscriber = new Subscriber();
            subscriber.setSender(ownUser);
            subscriber.setReceiver(reciverUser);
            subscriber.setFriends(false);
            subscribeRepository.save(subscriber);
        }
        if (subscribeRepository.existsBySender_IdAndReceiver_Id(reciverUser.getId(), ownUser.getId())) {
            approveFriend(username, userId);
        } else {
            messengerService.sendMessage(username, RequestMessageDto.builder()
                    .message("Запрос на дружбу от:" + username)
                    .receiver(userId)
                    .build());
        }
    }

    @Override
    @Transactional
    public void approveFriend(String username, UUID userId) {
        User ownUser = userService.findUserByUsername(username);
        User reciverUser = userService.findUserByUserId(userId);
        Subscriber subscriber;
        if (!subscribeRepository.existsBySender_IdAndReceiver_Id(ownUser.getId(), reciverUser.getId())) {
            subscriber = new Subscriber();
            subscriber.setSender(ownUser);
            subscriber.setReceiver(reciverUser);
            subscriber.setFriends(true);
            subscribeRepository.save(subscriber);
        } else {
            subscriber = findBySenderIdAndReceiverId(ownUser.getId(), reciverUser.getId());
            subscriber.setFriends(true);
            subscribeRepository.save(subscriber);
        }
        subscriber = findBySenderIdAndReceiverId(reciverUser.getId(), ownUser.getId());
        subscriber.setFriends(true);
        subscribeRepository.save(subscriber);
        messengerService.sendMessage(reciverUser.getUsername(), RequestMessageDto.builder()
                .message("Поздравляю, мы стали друзьями!")
                .receiver(ownUser.getId())
                .build());
        messengerService.sendMessage(username, RequestMessageDto.builder()
                .message("Поздравляю, мы стали друзьями!")
                .receiver(userId)
                .build());
    }

    @Override
    public void deleteFromFriend(String username, UUID userId) {
        User ownUser = userService.findUserByUsername(username);

        Subscriber subscriber = findBySenderIdAndReceiverId(ownUser.getId(), userId);
        subscribeRepository.delete(subscriber);

        subscriber = findBySenderIdAndReceiverId(userId, ownUser.getId());
        subscriber.setFriends(false);
        subscribeRepository.save(subscriber);
    }

    @Override
    public void unsubscribe(String username, UUID userId) {
        User sender = userService.findUserByUsername(username);
        Subscriber subscriber = findBySenderIdAndReceiverId(sender.getId(), userId);
        if (subscriber.isFriends()) {
            deleteFromFriend(username, userId);
        } else {
            subscribeRepository.delete(subscriber);
        }
    }

    @Override
    public List<UserResponseDto> getFrendsList(String username) {
        User sender = userService.findUserByUsername(username);
        List<Subscriber> subscribers = subscribeRepository.findBySender_IdAndIsFriends(sender.getId(), true);
        return subscribers.stream()
                .map(s -> userConverter.fromEntityToRespDto(s.getReceiver()))
                .collect(Collectors.toList());
    }

    private Subscriber findBySenderIdAndReceiverId(UUID sender, UUID receiver) {
        return subscribeRepository.findBySender_IdAndReceiver_Id(sender, receiver)
                .orElseThrow(() -> new ResourceNotFoundException("Subscribe not found"));
    }
}
