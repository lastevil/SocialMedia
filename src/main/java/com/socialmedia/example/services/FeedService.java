package com.socialmedia.example.services;

import com.socialmedia.example.converters.PostMapper;
import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.repositorys.PostRepository;
import com.socialmedia.example.services.interfaces.FeedServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService implements FeedServiceImpl {
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public Page<ResponsePostDto> getFeed(String username, int page, int size, boolean timeUp) {
        if (size < 1) {
            size = 1;
        }
        Pageable sortedByName;
        if (timeUp) {
            sortedByName = PageRequest.of(page - 1, size, Sort.by("createdAt"));
        } else {
            sortedByName = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        }
        UUID uuid = userService.findUserByUsername(username).getId();
        return postRepository.findByUserId(uuid, sortedByName)
                .map(PostMapper.INSTANCE::fromEntityToResp);
    }

    @Override
    public List<ResponsePostDto> getPostsByUser(UUID userId) {
        return postRepository.findAllByUserId(userId).stream()
                .map(PostMapper.INSTANCE::fromEntityToResp)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponsePostDto> getCurrentUserPost(String username) {
        UUID uuid = userService.findUserByUsername(username).getId();
        return getPostsByUser(uuid);
    }
}
